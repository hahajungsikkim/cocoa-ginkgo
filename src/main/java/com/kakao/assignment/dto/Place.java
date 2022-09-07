package com.kakao.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Place {
    private String title;
    private String address;
    private String road_address;
}
