package com.kakao.assignment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    ExceptionEnum exceptionEnum;
}
