package com.kakao.assignment.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
public class CacheConfiguration {

    public static final String PLACE = "place";
    public static final String KEYWORD = "keyword";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(PLACE, KEYWORD);
    }

    @CacheEvict(allEntries = true, value = {PLACE}) // flush cache
    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 500) // each 10 minutes
    public void reportCacheEvict() {
        log.info("flush cache [target=" + PLACE + "]");
    }
}
