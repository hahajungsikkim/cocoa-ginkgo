package com.kakao.assignment.service;

import com.kakao.assignment.adapter.SearchApiAdapter;
import com.kakao.assignment.dto.KeywordResult;
import com.kakao.assignment.dto.Place;
import com.kakao.assignment.dto.PlaceResult;
import com.kakao.assignment.entity.KeywordManagement;
import com.kakao.assignment.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchApiAdapter kakaoSearchApiAdapter;
    private final SearchApiAdapter naverSearchApiAdapter;
    private final KeywordRepository keywordRepository;

    public static final String PLACE = "place";
    public static final String KEYWORD = "keyword";

    @Value("${openapi.kakao.search-count}")
    private int KAKAO_SEARCH_COUNT;
    @Value("${openapi.naver.search-count}")
    private int NAVER_SEARCH_COUNT;

    @Cacheable(value = {PLACE}, key = "#keyword")
    public PlaceResult getPlaceList(String keyword) {
        // 1. search with openapi
        List<Place> kakaoPlaces = kakaoSearchApiAdapter.searchFromOpenApi(keyword, KAKAO_SEARCH_COUNT);
        List<Place> naverPlaces = naverSearchApiAdapter.searchFromOpenApi(keyword, NAVER_SEARCH_COUNT);

        // 2. find duplicated list (condition = replace empty space)
        Set<String> distinctPlaces = getDistinctPlaces(kakaoPlaces, naverPlaces);

        // 3. fill result (1st = duplicated places, 2nd = kakao places, 3rd = naver places)
        PlaceResult result = new PlaceResult();
        result.addPlaces(getDuplicatedPlaces(kakaoPlaces, distinctPlaces, true)); // 중복되는 장소 1순위
        result.addPlaces(getDuplicatedPlaces(kakaoPlaces, distinctPlaces, false)); // 카카오 장소 2순위
        result.addPlaces(getDuplicatedPlaces(naverPlaces, distinctPlaces, false)); // 네이버 장소 3순위

        return result;
    }

    @SafeVarargs
    private Set<String> getDistinctPlaces(List<Place>... placesList) {
        List<String> places = new ArrayList<>();
        for (List<Place> pl : placesList) {
            places.addAll(pl.stream().map(p -> p.getTitle().replaceAll(" ", "")).collect(Collectors.toList()));
        }

        return places.stream()
                .filter(p -> Collections.frequency(places, p) > 1)
                .collect(Collectors.toSet());
    }

    private List<Place> getDuplicatedPlaces(List<Place> places, Set<String> distinctPlaces, boolean isDuplicated) {
        if (isDuplicated) {
            return places.stream()
                    .filter(place -> distinctPlaces.contains(place.getTitle().replaceAll(" ", "")))
                    .collect(Collectors.toList());
        } else {
            return places.stream()
                    .filter(place -> !distinctPlaces.contains(place.getTitle().replaceAll(" ", "")))
                    .collect(Collectors.toList());
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(value = {KEYWORD})
    public KeywordResult getKeywordList() {
        final List<KeywordManagement> keywordManagements = keywordRepository.findTop10ByOrderByCountDescKeywordAsc();

        KeywordResult result = new KeywordResult();
        result.addKeywords(keywordManagements);

        return result;
    }

    @Transactional
    @CacheEvict(allEntries = true, value = {KEYWORD})
    public void saveKeyword(String keyword) {
        KeywordManagement keywordManagement = keywordRepository.findByIdForUpdate(keyword).orElse(
                new KeywordManagement(keyword, 0)
        );
        keywordManagement.plusCount();
        keywordRepository.save(keywordManagement);
    }
}