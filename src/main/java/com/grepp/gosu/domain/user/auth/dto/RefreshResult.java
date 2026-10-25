package com.grepp.gosu.domain.user.auth.dto;

import com.grepp.gosu.global.security.CustomUserDetails;

// Refresh 토큰 교체 결과를 AuthService에 전달한다.
public record RefreshResult(CustomUserDetails userDetails, String refreshToken) {
}
