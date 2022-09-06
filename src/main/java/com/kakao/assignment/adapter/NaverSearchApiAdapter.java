package com.kakao.assignment.adapter;

import com.kakao.assignment.dto.Place;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverSearchApiAdapter implements SearchApiAdapter {

    private final RestTemplate restTemplate;

    @Value("${openapi.naver.url}")
    private String URL;
    @Value("${openapi.naver.client-id}")
    private String CLIENT_ID;
    @Value("${openapi.naver.client-secret}")
    private String CLIENT_SECRET;

    @Value("${openapi.naver.place.objects}")
    private String OBJECT_ARRAY;
    @Value("${openapi.naver.place.name}")
    private String NAME;
    @Value("${openapi.naver.place.address}")
    private String ADDRESS;
    @Value("${openapi.naver.place.address-road}")
    private String ADDRESS_ROAD;

    @Value("${common.html-regex}")
    private String HTML_REGEX;

    @Override
    public List<Place> searchFromOpenApi(String keyword, int count) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", CLIENT_ID);
        headers.set("X-Naver-Client-Secret", CLIENT_SECRET);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("query", keyword)
                .queryParam("display", count);

        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

        return convertToPlace(response.getBody());
    }

    private List<Place> convertToPlace(String jsonString) {
        if ("fail".equals(jsonString)) {
            return new ArrayList<>();
        }
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
            JSONArray documents = (JSONArray) jsonObject.get(OBJECT_ARRAY);
            return (List<Place>) documents.stream()
                    .map(document -> {
                        JSONObject documentJO = (JSONObject) document;
                        return new Place(
                                String.valueOf(documentJO.get(NAME)).replaceAll(HTML_REGEX, ""),
                                String.valueOf(documentJO.get(ADDRESS)).replaceAll(HTML_REGEX, ""),
                                String.valueOf(documentJO.get(ADDRESS_ROAD)).replaceAll(HTML_REGEX, "")
                        );
                    })
                    .collect(Collectors.toList());
        } catch (ParseException e) {
            log.error("Naver Open Api Parsing Error = " + e.getMessage());
        }

        return new ArrayList<>();
    }
}
