package com.grepp.gosu.domain.user.auth.dto;

// 서비스의 두 토큰을 컨트롤러로 전달한다. 응답 본문으로 직접 반환하지 않는다.
public record LoginResult(
        String accessToken,
        String refreshToken
) {
}
