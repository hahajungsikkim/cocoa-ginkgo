package com.kakao.assignment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ExceptionEnum {
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "err.001", "Internal Server Error"),
    NO_REQUIRED_DATA(HttpStatus.BAD_REQUEST, "err.006", "필수 데이터가 존재하지 않습니다."),
    INPUT_DATA_INVALID(HttpStatus.BAD_REQUEST, "err.007", "입력 데이터가 올바르지 않습니다."),
    SCRAP_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "err.009", "스크랩 정보를 가져올 수 없습니다."),
    NO_DATA(HttpStatus.INTERNAL_SERVER_ERROR, "err.010", "데이터가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}

