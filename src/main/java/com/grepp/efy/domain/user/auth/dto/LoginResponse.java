package com.grepp.efy.domain.user.auth.dto;

public record LoginResponse(
        String accessToken,
        String tokenType
) {
}
