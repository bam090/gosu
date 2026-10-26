package com.grepp.gosu.domain.user.auth;

import com.grepp.gosu.domain.user.auth.dto.LoginRequest;
import com.grepp.gosu.domain.user.auth.dto.LoginResult;
import com.grepp.gosu.domain.user.auth.dto.RefreshResult;
import com.grepp.gosu.domain.user.member.Member;
import com.grepp.gosu.domain.user.member.MemberMapper;
import com.grepp.gosu.domain.user.member.MemberRepository;
import com.grepp.gosu.domain.user.member.MemberService;
import com.grepp.gosu.domain.user.member.dto.MemberCreateRequest;
import com.grepp.gosu.domain.user.member.dto.MemberResponse;
import com.grepp.gosu.global.exception.BusinessException;
import com.grepp.gosu.global.exception.ErrorCode;
import com.grepp.gosu.global.security.CustomUserDetails;
import com.grepp.gosu.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    // TODO: 비밀번호를 잊은 회원을 위해 본인 확인 후 비밀번호를 재설정하는 API를 추가합니다.

    // 일반 사용자 회원가입
    @Transactional
    public MemberResponse signup(MemberCreateRequest memberCreateRequest) {

        Member member = memberService.create(memberCreateRequest);
        return memberMapper.toResponse(member);
    }

    @Transactional
    public LoginResult login(LoginRequest loginRequest) {

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED_EXCEPTION);
        }

        if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED_EXCEPTION);
        }

        String accessToken = jwtProvider.createToken(userDetails);
        String refreshToken = refreshTokenService.issued(userDetails.getMemberId());
        return new LoginResult(accessToken, refreshToken);
    }

    @Transactional
    public LoginResult refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN_EXCEPTION);
        }

        RefreshResult refreshResult = refreshTokenService.refresh(refreshToken);

        String accessToken = jwtProvider.createToken(refreshResult.userDetails());
        return new LoginResult(accessToken, refreshResult.refreshToken());
    }

    @Transactional
    public void logout(Long memberId) {
        Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION));

        refreshTokenRepository.deleteByMemberId(memberId);

        // 같은 초에 발급된 Access 토큰까지 무효화한다.
        member.setTokenInValidBefore(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusSeconds(1));
    }
}
