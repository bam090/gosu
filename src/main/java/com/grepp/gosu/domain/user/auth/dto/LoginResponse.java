package com.grepp.gosu.domain.user.auth.dto;

public record LoginResponse(
        String accessToken,
        String tokenType
) {
}
