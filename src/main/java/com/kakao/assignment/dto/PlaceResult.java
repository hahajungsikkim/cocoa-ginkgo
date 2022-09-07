package com.kakao.assignment.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlaceResult {
    private final List<Place> places = new ArrayList<>();

    public void addPlaces(List<Place> placeList) {
        this.places.addAll(placeList);
    }
}
