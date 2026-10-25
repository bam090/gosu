package com.grepp.gosu.domain.user.auth;

import com.grepp.gosu.domain.user.member.Member;
import com.grepp.gosu.domain.user.member.MemberMapper;
import com.grepp.gosu.domain.user.member.MemberService;
import com.grepp.gosu.domain.user.member.dto.MemberCreateRequest;
import com.grepp.gosu.domain.user.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    // 일반 사용자 회원가입
    @Transactional
    public MemberResponse signup(MemberCreateRequest memberCreateRequest) {
        Member member = memberService.create(memberCreateRequest);

        return memberMapper.toResponse(member);
    }
}
