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

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager(PLACE);
        return cacheManager;
    }

    @CacheEvict(allEntries = true, value = {PLACE})
    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 500) // flush cache each 10 minutes
    public void reportCacheEvict() {
        log.info("flush cache");
    }
}
