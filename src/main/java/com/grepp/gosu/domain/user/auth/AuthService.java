package com.grepp.gosu.domain.user.auth;

import com.grepp.gosu.domain.user.auth.dto.LoginRequest;
import com.grepp.gosu.domain.user.auth.dto.LoginResponse;
import com.grepp.gosu.domain.user.member.Member;
import com.grepp.gosu.domain.user.member.MemberMapper;
import com.grepp.gosu.domain.user.member.MemberService;
import com.grepp.gosu.domain.user.member.dto.MemberCreateRequest;
import com.grepp.gosu.domain.user.member.dto.MemberResponse;
import com.grepp.gosu.global.exception.BusinessException;
import com.grepp.gosu.global.exception.ErrorCode;
import com.grepp.gosu.global.security.CustomUserDetails;
import com.grepp.gosu.global.security.JwtProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    // 일반 사용자 회원가입
    @Transactional
    public MemberResponse signup(MemberCreateRequest memberCreateRequest) {

        Member member = memberService.create(memberCreateRequest);
        return memberMapper.toResponse(member);
    }

    public LoginResponse login(@Valid LoginRequest loginRequest) {

        Authentication authentication;

        try {
            // 회원 조회와 비밀번호 비교를 시큐리티에 맡긴다.
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            // 이메일 없음과 비밀번호 불일치를 같은 응답으로 처리한다.
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String accessToken = jwtProvider.createToken(userDetails);
        return new LoginResponse(accessToken, "Bearer");
    }
}
