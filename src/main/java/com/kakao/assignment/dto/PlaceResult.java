package com.kakao.assignment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PlaceResult {
    private List<Place> places = new ArrayList<>();

    public void addPlaces(List<Place> placeList) {
        this.places.addAll(placeList);
    }
}
