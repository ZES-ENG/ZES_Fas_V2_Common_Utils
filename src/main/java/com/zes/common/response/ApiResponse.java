package com.zes.common.response;

import com.zes.common.util.AppConstants;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class ApiResponse<T> {
    private String message;
    private T data;
    private HttpStatus status;

    // 기본 생성자
    public ApiResponse(T data) {
        this.message = AppConstants.SUCCESS.getValue();
        this.data = data;
        this.status = HttpStatus.OK;
    }

    // 상태 코드와 메시지, 데이터 설정 생성자
    public ApiResponse(HttpStatus status, String message, T data) {
        this.message = message;
        this.data = data;
        this.status = status;
    }

    // 상태 코드와 메시지, 데이터 설정 팩토리 메서드
    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    // 상태 코드만 지정하고 데이터 설정 팩토리 메서드
    public static <T> ApiResponse<T> of(HttpStatus status, T data) {
        return new ApiResponse<>(status, status.name(), data);
    }

    public static <T> ApiResponse<T> ok() {
        return ApiResponse.ok(null);
    }
    // OK 응답을 반환하는 메서드
    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.of(HttpStatus.OK, HttpStatus.OK.name(), data);
    }

    // ResponseEntity로 반환하는 메서드
    public ResponseEntity<ApiResponse<T>> toResponseEntity() {
        return ResponseEntity.status(this.status).body(this);
    }
}
