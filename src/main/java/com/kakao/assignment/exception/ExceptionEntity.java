package com.kakao.assignment.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@Builder
public class ExceptionEntity {
    private String code;
    private String message;
    private String detail;

    public static ResponseEntity<ExceptionEntity> toResponseEntity(ExceptionEnum e) {
        return ResponseEntity
                .status(e.getStatus())
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(ExceptionEntity.builder()
                        .code(e.getCode())
                        .message(e.getMessage())
                        .detail("")
                        .build()
                );
    }
}
