package com.grepp.gosu.domain.user.auth;

import com.grepp.gosu.domain.user.auth.dto.LoginRequest;
import com.grepp.gosu.domain.user.auth.dto.LoginResponse;
import com.grepp.gosu.domain.user.auth.dto.LoginResult;
import com.grepp.gosu.domain.user.member.dto.MemberCreateRequest;
import com.grepp.gosu.domain.user.member.dto.MemberResponse;
import com.grepp.gosu.global.security.RefreshTokenCookie;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원·인증", description = "회원가입과 이메일 로그인 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenCookie refreshTokenCookie;

    @SecurityRequirements // Swagger: 이 API는 인증 없이 호출
    @Operation(summary = "회원가입")
    @PostMapping("/public/auth/signup")
    public ResponseEntity<MemberResponse> signup(
            @Valid @RequestBody MemberCreateRequest memberCreateRequest
    ) {
        MemberResponse memberResponse = authService.signup(memberCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberResponse);
    }

    @SecurityRequirements
    @Operation(summary = "로그인")
    @PostMapping("/public/auth/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        LoginResult loginResult = authService.login(loginRequest);
        LoginResponse loginResponse = new LoginResponse(loginResult.accessToken(), "Bearer");
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        refreshTokenCookie.create(loginResult.refreshToken()).toString())
                .body(loginResponse);
    }
}
