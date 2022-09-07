### 1. 서비스 요구사항

1) 장소 검색

###### 요청예시

```http request
GET http://localhost:8080/v1/place?keyword=스테이크
```

###### 응답예시

```json
{
  "status": "success",
  "data": {
    "places": [
      {
        "title": "울프강스테이크하우스",
        "address": "서울 강남구 청담동 89-6",
        "road_address": "서울 강남구 선릉로152길 21"
      }
    ]
  },
  "error": null
}
```

2) 검색 키워드 목록

###### 요청예시

```http request
GET http://localhost:8080/v1/keyword
```

###### 응답예시

```json
{
  "status": "success",
  "data": {
    "keywords": [
      {
        "keyword": "스타필드",
        "count": 8
      }
    ]
  },
  "error": null
}
```

### 2. 기술 요구사항

* 외부 라이브러리 및 오픈소스 사용 가능
    * 과제 코드에서 직접 활용한 라이브러리에 대해 사용 목적과 선택 사유 등을 README 파일에 명시

```
jasypt-spring-boot-starter 사용 : 과제에서는 k8s의 secret같은것을 활용할 수 없기에 해당 라이브러리를 통해 application.yaml에 있는 민감정보 등을 암호화하여 사용
```

* 구현한 API 테스트 방법 작성

    * cURL 로 테스트하는 방법을 README 파일에 추가 혹은 HTTP Request file을 작성하여 프로젝트에 추가

```
장소 검색 API 테스트 : api-test-place.http 파일 참고
검색 키워드 목록 API 테스트 : api-test-keyword.http 파일 참고
```

* 동시성 이슈가 발생할 수 있는 부분을 염두에 둔 설계 및 구현 (예시. 키워드 별로 검색된 횟수)

```
spring event + pessimisitc lock 으로 구현
* 과제에서는 Kafka를 활용하기 어렵기에 Spring Event로 pub/sub 구조만 설계

참고 패키지 : com.kakao.assignment.event (spring event)
참고 인터페이스 : com.kakao.assignment.repository.KeywordRepository (pessimistic lock)
```

* 카카오, 네이버 등 검색 API 제공자의 “다양한” 장애 발생 상황에 대한 고려

```
spring retry 를 사용
@Retryable 을 통해 API 호출 재시도 수행
@Recover 를 통해 API 호출 실패시 대응방안 마련

참고 클래스 : com.kakao.assignment.config.RestTemplateConfiguration
```

* 구글 장소 검색 등 새로운 검색 API 제공자의 추가 시 변경 영역 최소화에 대한 고려

```
SearchApiAdaper interface 를 상속받는 GoogleSearchApiAdapter(가칭)을 구현하여
SearchService.getPlaceList 에서 Kakao, Naver 처럼 호출하는 부분을 추가해주면
새로운 검색 API 제공자의 추가에 대한 대응 가능

참고 패키지 : com.kakao.assingment.adapter
```

* 서비스 오류 및 장애 처리 방법에 대한 고려

```
공통 예외 처리를 활용해 client와 error code에 대해 사전 정의 가능

참고 패키지 : con.kakao.assignment.exception
```

* 대용량 트래픽 처리를 위한 반응성(Low Latency), 확장성(Scalability), 가용성(Availability)을 높이기 위한 고려

```
Cache를 활용해서 서버의 부하 방지 및 빠른 응답
read-only transaction으로 메모리 사용량 최적화

참고 클래스 : com.kakao.assignment.config.CacheConfiguration (cache)
참고 메소드 : SearchService.getPlaceList (cache)
참고 메소드 : SearchService.getKeywordList (cache, read-only transaction)
참고 메소드 : SearchService.saveKeyword (cache)
```
