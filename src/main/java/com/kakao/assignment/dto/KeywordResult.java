package com.kakao.assignment.dto;

import com.kakao.assignment.entity.KeywordManagement;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class KeywordResult {
    private final List<KeywordManagement> keywords = new ArrayList<>();

    public void addKeywords(List<KeywordManagement> keywordList) {
        this.keywords.addAll(keywordList);
    }
}
