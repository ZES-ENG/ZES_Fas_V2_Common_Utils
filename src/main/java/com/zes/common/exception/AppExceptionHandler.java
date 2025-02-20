package com.zes.common.exception;

import com.zes.common.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {

    // Handle AppException
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppException(AppException ex) {
        // AppException에서 상태 코드와 메시지를 추출하여 ApiResponse 반환
        ApiResponse<Void> apiResponse = new ApiResponse<>(ex.getStatus(), ex.getMessage(), null);
        return apiResponse.toResponseEntity();
    }

    // Handle RuntimeException (General exception)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException ex) {
        // GENERAL ERROR로 INTERNAL_SERVER_ERROR 상태 코드 설정
        ApiResponse<Void> apiResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
        return apiResponse.toResponseEntity();
    }

    // Handle ConstraintViolationException (Validation errors)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException ex) {
        // Validation error 메시지 가져오기
        String firstViolationMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("Validation error");

        ApiResponse<Void> apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST, firstViolationMessage, null);
        return apiResponse.toResponseEntity();
    }

    // Handle MethodArgumentNotValidException (Validation errors from annotations)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // 첫 번째 필드 오류 메시지 가져오기
        String firstErrorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation error");

        ApiResponse<Void> apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST, firstErrorMessage, null);
        return apiResponse.toResponseEntity();
    }
}
