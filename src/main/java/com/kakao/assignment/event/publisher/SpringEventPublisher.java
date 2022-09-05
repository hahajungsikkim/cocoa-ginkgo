package com.kakao.assignment.event.publisher;

import com.kakao.assignment.event.KeywordSaveEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishKeywordSaveEvent(final String keyword) {
        KeywordSaveEvent keywordSaveEvent = new KeywordSaveEvent(this, keyword);
        applicationEventPublisher.publishEvent(keywordSaveEvent);
    }
}
