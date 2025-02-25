package com.zes.common.response;

import com.zes.common.util.AppConstants;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

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

    // ✅ 서블릿 기반(Spring MVC)에서 사용할 응답 생성
    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> of(HttpStatus status, T data) {
        return new ApiResponse<>(status, status.name(), data);
    }

    public static <T> ApiResponse<T> ok() {
        return ApiResponse.ok(null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.of(HttpStatus.OK, AppConstants.SUCCESS.getValue(), data);
    }

    // ✅ ResponseEntity로 변환 (서블릿 기반에서 사용)
    public ResponseEntity<ApiResponse<T>> toResponseEntity() {
        return ResponseEntity.status(this.status).body(this);
    }

    // ✅ WebFlux용 응답을 생성하는 팩토리 메서드 (WebFlux 환경에서 사용)
    public static <T> Mono<ApiResponse<T>> toMono(HttpStatus status, String message, T data) {
        return Mono.just(new ApiResponse<>(status, message, data));
    }

    public static <T> Mono<ApiResponse<T>> toMonoOk(T data) {
        return toMono(HttpStatus.OK, AppConstants.SUCCESS.getValue(), data);
    }
}
