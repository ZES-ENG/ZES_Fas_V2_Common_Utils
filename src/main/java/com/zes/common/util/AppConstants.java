package com.zes.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppConstants {
    // 상태 관련 상수
    ACTIVE_STATUS("active"),
    SUCCESS("success"),

    // 에러 메시지 상수들
    ERROR_SERVER_ERROR("SERVER ERROR"),
    ERROR_NOT_FOUND("사용자를 찾을 수가 없습니다."),
    ERROR_DUPLICATE_ID("아이디가 중복 되었습니다."),
    ERROR_INVALID_CREDENTIALS("해당 데이터가 존재하지 않습니다."),

    // 유효성 검사 메시지
    VALIDATION_NAME_REQUIRED("이름은 필수입니다."),
    VALIDATION_EMAIL_REQUIRED("email은 필수입니다."),
    VALIDATION_EMAIL_FORMAT("올바른 이메일 형식이 아닙니다."),
    // 쿠키 관련 상수
    COOKIE_ACCESS_TOKEN("ACCESS_TOKEN"),
    COOKIE_REFRESH_TOKEN("REFRESH_TOKEN");

    private final String value;
}

