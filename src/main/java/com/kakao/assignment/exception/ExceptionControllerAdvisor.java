package com.kakao.assignment.exception;

import com.kakao.assignment.dto.common.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@AllArgsConstructor
@ControllerAdvice
public class ExceptionControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ApiResponse> handleCustomException(CustomException e) {
        return ExceptionEntity.toResponseEntity(e.getExceptionEnum());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse> handleException(Exception e) {
        log.error(e.getMessage(), e);

        ExceptionEnum exEnum = ExceptionEnum.INTERNAL_ERROR;

        return ExceptionEntity.toResponseEntity(exEnum);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        FieldError fieldError = e.getBindingResult().getFieldError();

        ExceptionEnum exEnum = ExceptionEnum.NO_REQUIRED_DATA;

        return ResponseEntity
                .status(exEnum.getStatus())
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(ApiResponse.createError(ExceptionEntity.builder()
                        .code(exEnum.getCode())
                        .message(exEnum.getMessage())
                        .detail(fieldError != null ? fieldError.getDefaultMessage() : "")
                        .build())
                );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(e.getMessage(), e);

        ExceptionEnum exEnum = ExceptionEnum.INTERNAL_ERROR;
        ExceptionEntity exEntity = ExceptionEntity.builder()
                .code(exEnum.getCode())
                .message(exEnum.getMessage())
                .build();

        return super.handleExceptionInternal(e, ApiResponse.createError(exEntity), headers, exEnum.getStatus(), request);
    }

}
