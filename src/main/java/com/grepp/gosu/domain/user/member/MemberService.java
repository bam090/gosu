package com.grepp.gosu.domain.user.member;

import com.grepp.gosu.domain.user.member.dto.MemberCreateRequest;
import com.grepp.gosu.global.exception.BusinessException;
import com.grepp.gosu.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;


    @Transactional
    public Member create(MemberCreateRequest memberCreateRequest) {
        // 중복된 이메일과 닉네임은 가입을 거절한다.
        if (memberRepository.existsByEmail(memberCreateRequest.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        if (memberRepository.existsByNickname(memberCreateRequest.getNickname())) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }

        Member member = memberMapper.toEntity(memberCreateRequest);

        // 비밀번호는 해시로 저장하고 가입 권한은 서버에서 정한다.
        member.setPassword(passwordEncoder.encode(memberCreateRequest.getPassword()));
        member.setRole(MemberRole.CUSTOMER);

        return memberRepository.save(member);
    }
}
