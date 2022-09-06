package com.kakao.assignment.dto.common;

import com.kakao.assignment.exception.ExceptionEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";

    private String status;
    private T data;
    private ExceptionEntity error;

    public static <T> ApiResponse<T> createSuccess(T data) {
        return new ApiResponse<>(SUCCESS_STATUS, data, null);
    }

    public static ApiResponse<?> createSuccessWithNoContent() {
        return new ApiResponse<>(SUCCESS_STATUS, null, null);
    }

    public static ApiResponse<?> createError(ExceptionEntity error) {
        return new ApiResponse<>(ERROR_STATUS, null, error);
    }

    private ApiResponse(String status, T data, ExceptionEntity error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }
}