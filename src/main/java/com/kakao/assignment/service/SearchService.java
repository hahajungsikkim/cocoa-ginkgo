package com.kakao.assignment.service;

import com.kakao.assignment.adapter.SearchApiAdapter;
import com.kakao.assignment.entity.KeywordManagement;
import com.kakao.assignment.event.publisher.SpringEventPublisher;
import com.kakao.assignment.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
//@Transactional
@Slf4j
public class SearchService {

    private final SearchApiAdapter kakaoSearchApiAdapter;
    private final SearchApiAdapter naverSearchApiAdapter;
    private final SpringEventPublisher springEventPublisher;
    private final KeywordRepository keywordRepository;

    public String getPlaceList(String keyword) {
        // 1. keyword save
        springEventPublisher.publishKeywordSaveEvent(keyword);

        // 2. search with openapi
        kakaoSearchApiAdapter.searchFromOpenApi(keyword, 0);
        naverSearchApiAdapter.searchFromOpenApi(keyword, 5);
        return "";
    }

    public String getKeywordList() {

        return "";
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