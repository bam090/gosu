package com.grepp.gosu.global.exception;

import lombok.Getter;

// 서비스에서 던지는 모든 커스텀 예외는 이 클래스 하나로 던진다
// 예) throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
