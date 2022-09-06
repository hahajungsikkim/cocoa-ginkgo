package com.kakao.assignment.adapter;

import com.kakao.assignment.dto.Place;

import java.util.List;

public interface SearchApiAdapter {

    List<Place> searchFromOpenApi(String keyword, int count);
}
