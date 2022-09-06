package com.kakao.assignment.controller;

import com.kakao.assignment.dto.common.ApiResponse;
import com.kakao.assignment.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping(value = "/v1/place")
    public ResponseEntity<ApiResponse<?>> place(@RequestParam String keyword) {

        return ResponseEntity.ok().body(ApiResponse.createSuccess(searchService.getPlaceList(keyword)));
    }

    @GetMapping(value = "/v1/keyword")
    public ResponseEntity<ApiResponse<?>> keyword() {

        return ResponseEntity.ok().body(ApiResponse.createSuccess(searchService.getKeywordList()));
    }
}
