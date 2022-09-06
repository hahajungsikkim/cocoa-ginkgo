package com.kakao.assignment.exception;

import com.kakao.assignment.dto.common.ApiResponse;
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

    public static ResponseEntity<ApiResponse> toResponseEntity(ExceptionEnum e) {
        return ResponseEntity
                .status(e.getStatus())
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(ApiResponse.createError(ExceptionEntity.builder()
                        .code(e.getCode())
                        .message(e.getMessage())
                        .detail("")
                        .build())
                );
    }
}
