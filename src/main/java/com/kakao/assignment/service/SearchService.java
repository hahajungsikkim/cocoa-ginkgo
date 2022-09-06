package com.kakao.assignment.service;

import com.kakao.assignment.adapter.SearchApiAdapter;
import com.kakao.assignment.dto.Place;
import com.kakao.assignment.dto.PlaceResult;
import com.kakao.assignment.dto.common.ApiResponse;
import com.kakao.assignment.entity.KeywordManagement;
import com.kakao.assignment.event.publisher.SpringEventPublisher;
import com.kakao.assignment.exception.CustomException;
import com.kakao.assignment.exception.ExceptionEnum;
import com.kakao.assignment.repository.KeywordRepository;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//@Transactional
@Slf4j
public class SearchService {

    private final SearchApiAdapter kakaoSearchApiAdapter;
    private final SearchApiAdapter naverSearchApiAdapter;
    private final SpringEventPublisher springEventPublisher;
    private final KeywordRepository keywordRepository;

    @Value("${openapi.kakao.search-count}")
    private int KAKAO_SEARCH_COUNT;
    @Value("${openapi.naver.search-count}")
    private int NAVER_SEARCH_COUNT;

    //    public ApiResponse<List<Place>> getPlaceList(String keyword) {
    public PlaceResult getPlaceList(String keyword) {
        // 1. keyword save
        springEventPublisher.publishKeywordSaveEvent(keyword);

        // 2. search with openapi
        //TODO: count check, distinct, sort algorithm
        List<Place> kakaoPlaces = kakaoSearchApiAdapter.searchFromOpenApi(keyword, KAKAO_SEARCH_COUNT);
        List<Place> naverPlaces = naverSearchApiAdapter.searchFromOpenApi(keyword, NAVER_SEARCH_COUNT);
        Set<String> distinctPlaces = getDistinctPlaces(kakaoPlaces, naverPlaces);

//        List<Place> places = new ArrayList<>();
//        places.addAll(getDuplicatedPlaces(kakaoPlaces, distinctPlaces, true)); // 중복되는 장소 1순위
//        places.addAll(getDuplicatedPlaces(kakaoPlaces, distinctPlaces, false)); // 카카오 장소 2순위
//        places.addAll(getDuplicatedPlaces(naverPlaces, distinctPlaces, false)); // 네이버 장소 3순위
//        return ApiResponse.createSuccess(places);

        PlaceResult result = new PlaceResult();
        result.addPlaces(getDuplicatedPlaces(kakaoPlaces, distinctPlaces, true)); // 중복되는 장소 1순위
        result.addPlaces(getDuplicatedPlaces(kakaoPlaces, distinctPlaces, false)); // 카카오 장소 2순위
        result.addPlaces(getDuplicatedPlaces(naverPlaces, distinctPlaces, false)); // 네이버 장소 3순위

        return result;
    }

    private Set<String> getDistinctPlaces(@NotNull List<Place>... placesList) {
        List<String> places = new ArrayList<>();
        for (List<Place> pl : placesList) {
            places.addAll(pl.stream().map(p -> p.getTitle()).collect(Collectors.toList()));
        }

//        return places.stream()
//                .collect(Collectors.toMap(Place::getTitle, Function.identity(), (p1, p2) -> p1))
//                .keySet();

        return places.stream()
                .filter(p -> Collections.frequency(places, p) > 1)
                .collect(Collectors.toSet());
    }

    private List<Place> getDuplicatedPlaces(List<Place> places, Set<String> distinctPlaces, boolean isDuplicated) {
        if (isDuplicated) {
            return places.stream()
                    .filter(place -> distinctPlaces.contains(place.getTitle()))
                    .collect(Collectors.toList());
        } else {
            return places.stream()
                    .filter(place -> !distinctPlaces.contains(place.getTitle()))
                    .collect(Collectors.toList());
        }
    }

    public List<KeywordManagement> getKeywordList() {

        List<KeywordManagement> keywordManagements = keywordRepository.findTop10ByOrderByCountDesc();
        System.out.println("keywordManagements = " + keywordManagements);
        return keywordManagements;
    }

    @Transactional
    public void saveKeyword(String keyword) {
        KeywordManagement keywordManagement = keywordRepository.findByIdForUpdate(keyword).orElse(
                new KeywordManagement(keyword, 0)
        );
        //TODO
//        try {
//            Thread.sleep(600);
//        } catch (Exception e) {
//
//        }
        keywordManagement.plusCount();
//        try {
//            keywordRepository.saveAndFlush(keywordManagement);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        keywordRepository.save(keywordManagement);
    }
}