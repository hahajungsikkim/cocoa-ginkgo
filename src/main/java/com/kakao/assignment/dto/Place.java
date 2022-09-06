package com.kakao.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Place {
    private String title;
    private String address;
    private String road_address;
}
