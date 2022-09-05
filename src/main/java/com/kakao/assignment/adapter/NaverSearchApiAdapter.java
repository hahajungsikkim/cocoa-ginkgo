package com.kakao.assignment.adapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class NaverSearchApiAdapter implements SearchApiAdapter {

    @Value("${openapi.naver.url}")
    private String url;
    @Value("${openapi.naver.client-id}")
    private String clientId;
    @Value("${openapi.naver.client-secret}")
    private String clientSecret;

    @Override
    public void searchFromOpenApi(String keyword, int count) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("query", keyword)
                .queryParam("display", count);

        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

//        System.out.println("Naver response.getBody() = " + response.getBody());

    }
}
