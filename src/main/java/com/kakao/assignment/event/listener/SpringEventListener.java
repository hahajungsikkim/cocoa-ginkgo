package com.kakao.assignment.event.listener;

import com.kakao.assignment.event.KeywordSaveEvent;
import com.kakao.assignment.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringEventListener {

    private final SearchService searchService;

    @Async
    @EventListener
    public void handleEvent(KeywordSaveEvent event) {
        //TODO
        try {
            System.out.println(event.getKeyword());
            searchService.saveKeyword(event.getKeyword());
        } catch (Exception e) {
            System.out.println("E = " + e.getMessage());
        }
//        catch (DataIntegrityViolationException e) {
//            System.out.println("EEE = " + e.getMessage());
//        } catch (ConstraintViolationException e) {
//            System.out.println("EEEEE = " + e.getMessage());
//        }
    }
}