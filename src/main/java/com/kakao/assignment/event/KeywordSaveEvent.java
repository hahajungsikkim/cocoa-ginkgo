package com.kakao.assignment.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class KeywordSaveEvent extends ApplicationEvent {

    private final String keyword;

    public KeywordSaveEvent(Object source, String keyword) {
        super(source);
        this.keyword = keyword;
    }
}
