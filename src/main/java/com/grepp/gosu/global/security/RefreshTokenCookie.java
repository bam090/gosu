package com.grepp.gosu.global.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

// 리프레시 토큰을 HttpOnly 쿠키로 만들고 지운다 (BlogRest 그대로)
@Component
public class RefreshTokenCookie {
    public static final String NAME = "refreshToken";
    // 재발급 요청에만 쿠키가 붙도록 경로를 좁힌다
    private static final String PATH = "/api/public/auth/refresh";

    private final Long refreshExpirationMillis;

    public RefreshTokenCookie(@Value("${jwt.refresh-expiration}") Long refreshExpirationMillis) {
        this.refreshExpirationMillis = refreshExpirationMillis;
    }

    private ResponseCookie.ResponseCookieBuilder base(String value) {
        return ResponseCookie.from(NAME, value)
                .httpOnly(true)      // 자바스크립트가 읽을 수 없다
                .secure(true)        // HTTPS로만 전송한다 (localhost는 예외로 허용)
                .sameSite("Strict")  // 배포 시 FE·BE 도메인이 다르면 "None"으로 변경
                .path(PATH);
    }

    // 로그인, 회원가입, 재발급할 때 내려보낸다
    public ResponseCookie create(String token) {
        return base(token).maxAge(Duration.ofMillis(refreshExpirationMillis)).build();
    }

    // 값이 비어 있고 수명이 0인 쿠키로 덮어써서 지운다
    public ResponseCookie expire() {
        return base("").maxAge(0).build();
    }
}
