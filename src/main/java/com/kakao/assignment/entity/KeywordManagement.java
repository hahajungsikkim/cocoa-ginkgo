package com.kakao.assignment.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "keyword_management", indexes = {
        @Index(name = "keyword_count_1", columnList = "COUNT")
})
public class KeywordManagement {
    @Id
    @Column(name = "KEYWORD")
    private String keyword;

    @Column(name = "COUNT")
    private long count;

    protected KeywordManagement() {

    }

    public KeywordManagement(String keyword, long count) {
        this.keyword = keyword;
        this.count = count;
    }

    public void plusCount() {
        this.count++;
    }
}
