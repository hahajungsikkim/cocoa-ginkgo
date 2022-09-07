package com.kakao.assignment.controller;

import com.kakao.assignment.dto.common.ApiResponse;
import com.kakao.assignment.event.publisher.SpringEventPublisher;
import com.kakao.assignment.exception.CustomException;
import com.kakao.assignment.exception.ExceptionEnum;
import com.kakao.assignment.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final SpringEventPublisher springEventPublisher;

    @GetMapping(value = "/v1/place")
    public ResponseEntity<ApiResponse<?>> place(@RequestParam String keyword) {
        if (!StringUtils.hasText(keyword)) {
            throw new CustomException(ExceptionEnum.NO_REQUIRED_DATA);
        }

        springEventPublisher.publishKeywordSaveEvent(keyword);
        return ResponseEntity.ok().body(ApiResponse.createSuccess(searchService.getPlaceList(keyword)));
    }

    @GetMapping(value = "/v1/keyword")
    public ResponseEntity<ApiResponse<?>> keyword() {

        return ResponseEntity.ok().body(ApiResponse.createSuccess(searchService.getKeywordList()));
    }
}
