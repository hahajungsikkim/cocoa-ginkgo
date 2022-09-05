package com.kakao.assignment.adapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KakaoSearchApiAdapter implements SearchApiAdapter {

    @Value("${openapi.kakao.url}")
    private String url;
    @Value("${openapi.kakao.api-key}")
    private String apiKey;

    @Override
    public void searchFromOpenApi(String keyword, int count) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", apiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("query", keyword);

        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

//        System.out.println("Kakao response.getBody() = " + response.getBody());

    }
}
