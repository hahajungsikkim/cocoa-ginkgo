package com.kakao.assignment.adapter;

import com.kakao.assignment.dto.Place;
import com.kakao.assignment.util.RegexUtil;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoSearchApiAdapter implements SearchApiAdapter {

    private final RestTemplate restTemplate;

    @Value("${openapi.kakao.url}")
    private String URL;
    @Value("${openapi.kakao.api-key}")
    private String API_KEY;

    @Value("${openapi.kakao.place.objects}")
    private String OBJECT_ARRAY;
    @Value("${openapi.kakao.place.name}")
    private String NAME;
    @Value("${openapi.kakao.place.address}")
    private String ADDRESS;
    @Value("${openapi.kakao.place.address-road}")
    private String ADDRESS_ROAD;

    @Override
    public List<Place> searchFromOpenApi(String keyword, int count) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", API_KEY);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("query", keyword)
                .queryParam("size", count);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

        return convertToPlace(response.getBody());
    }

    private List<Place> convertToPlace(String jsonString) {
        if ("fail".equals(jsonString)) {
            log.error("Kakao Open Api Response Error");
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
                                RegexUtil.replaceString(String.valueOf(documentJO.get(NAME))),
                                RegexUtil.replaceString(String.valueOf(documentJO.get(ADDRESS))),
                                RegexUtil.replaceString(String.valueOf(documentJO.get(ADDRESS_ROAD)))
                        );
                    })
                    .collect(Collectors.toList());
        } catch (ParseException e) {
            log.error("Kakao Open Api Parsing Error = " + e.getMessage());
        }

        return new ArrayList<>();
    }
}
