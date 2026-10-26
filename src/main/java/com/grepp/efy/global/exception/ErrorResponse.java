package com.grepp.efy.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

// 실패 응답 형식 (BlogRest와 같다)
// errors가 비어 있으면 응답 JSON에서 빠진다
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponse(String message, Map<String, String> errors) {
    public ErrorResponse(String message) {
        this(message, Map.of());
    }
}
