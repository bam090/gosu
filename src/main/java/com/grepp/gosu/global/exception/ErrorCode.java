package com.grepp.gosu.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// 서비스에서 던지는 모든 에러의 상태 코드와 메시지를 모아 둔 곳
//
// [사용 규칙]
// 1. 각자 자기 도메인 구역 안에만 추가한다 (구역이 떨어져 있어서 git 충돌이 나지 않는다)
// 2. 추가하기 전에 같은 의미의 상수가 이미 있는지 먼저 검색한다 (예: MEMBER_NOT_FOUND는 모든 도메인에서 재사용)
// 3. 던질 때는 throw new BusinessException(ErrorCode.XXX); 한 줄이면 된다
//    GlobalExceptionHandler는 수정하지 않는다
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 공통
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    INVALID_JSON(HttpStatus.BAD_REQUEST, "요청 본문을 읽을 수 없습니다. JSON 형식을 확인해주세요."),
    INVALID_TYPE(HttpStatus.BAD_REQUEST, "경로 또는 쿼리 값의 타입이 올바르지 않습니다."),
    INVALID_PAGE_REQUEST(HttpStatus.BAD_REQUEST, "페이지 번호, 크기 또는 정렬 조건이 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    PATH_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 경로를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."),
    DATA_CONFLICT(HttpStatus.CONFLICT, "데이터 무결성 제약 조건 위반입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    // ① 회원·인증
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),

    // ② 기준 데이터

    // ③ 고수

    // ④ 커뮤니티

    // ⑤ 견적 요청·매칭

    // ⑥ 리뷰·찜·좋아요

    // ⑦ 크레딧·결제

    // ⑧ 채팅

    ;

    private final HttpStatus status;
    private final String message;
}
