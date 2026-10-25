package com.grepp.gosu.domain.user.auth;

import com.grepp.gosu.domain.user.member.dto.MemberCreateRequest;
import com.grepp.gosu.domain.user.member.dto.MemberResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @SecurityRequirements // Swagger: 이 API는 인증 없이 호출
    @PostMapping("/public/auth/signup")
    public ResponseEntity<MemberResponse> signup(
            @Valid @RequestBody MemberCreateRequest memberCreateRequest
    ) {
        MemberResponse memberResponse = authService.signup(memberCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberResponse);
    }

}
