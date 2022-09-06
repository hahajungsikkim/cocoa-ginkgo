package com.kakao.assignment.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Slf4j
@EnableRetry
@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate retryableRestTemplate() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setReadTimeout(8000);
        clientHttpRequestFactory.setConnectTimeout(8000);

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory) {
            @Override
            @Retryable(value = RestClientException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
            public <T> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables)
                    throws RestClientException {
                return super.exchange(url, method, requestEntity, responseType, uriVariables);
            }

            @Recover
            public <T> ResponseEntity<String> exchangeRecover(RestClientException e) {
                log.error(e.getMessage());
                return ResponseEntity.badRequest().body("fail");
            }
        };
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);

        return restTemplate;
    }
}
