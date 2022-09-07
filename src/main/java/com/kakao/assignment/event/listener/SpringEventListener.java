package com.kakao.assignment.event.listener;

import com.kakao.assignment.event.KeywordSaveEvent;
import com.kakao.assignment.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpringEventListener {

    private final SearchService searchService;

    @Async
    @EventListener
    public void handleEvent(KeywordSaveEvent event) {
        try {
            searchService.saveKeyword(event.getKeyword());
        } catch (RuntimeException e) {
            log.error("Keyword save event fail = " + e.getMessage());
        }
    }
}