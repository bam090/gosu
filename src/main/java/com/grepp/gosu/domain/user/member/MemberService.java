package com.grepp.gosu.domain.user.member;

import com.grepp.gosu.domain.user.auth.RefreshTokenRepository;
import com.grepp.gosu.domain.user.member.dto.*;
import com.grepp.gosu.global.common.PageResponse;
import com.grepp.gosu.global.exception.BusinessException;
import com.grepp.gosu.global.exception.ErrorCode;
import com.grepp.gosu.global.util.PageableFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private static final List<String> SORTABLE_FIELDS = List.of("id", "nickname", "email", "createdAt");
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public Member create(MemberCreateRequest memberCreateRequest) {

        ensureNotReserved(memberCreateRequest.getEmail(), memberCreateRequest.getNickname());
        if (memberRepository.existsByPhone(memberCreateRequest.getPhone())) {
            throw new BusinessException(ErrorCode.DUPLICATE_PHONE);
        }

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
        // 가입 시 일반 사용자 권한을 준다.
        member.setRole(MemberRole.CUSTOMER);
        return memberRepository.save(member);
    }

    public PageResponse<MemberResponse> getMembers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageable = PageableFactory.create(pageNumber, pageSize, sortBy, sortOrder, SORTABLE_FIELDS);
        Page<Member> page = memberRepository.findByDeletedAtIsNull(pageable);
        return PageResponse.of(page, page.getContent().stream().map(memberMapper::toResponse).toList());
    }

    public MemberResponse getCurrentMember(Long memberId) {
        Member member = getMemberEntity(memberId);
        return memberMapper.toResponse(member);
    }

    public MemberPublicResponse getMemberById(Long memberId) {
        Member member = getMemberEntity(memberId);
        return memberMapper.toPublicResponse(member);
    }

    @Transactional
    public MemberResponse updateMember(Long memberId, MemberUpdateRequest request) {
        Member member = getMemberEntity(memberId);

        if (request.getNickname() != null) {
            ensureNotReserved(null, request.getNickname());
            if (memberRepository.existsByNicknameAndIdNot(request.getNickname(), memberId)) {
                throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
            }
            member.setNickname(request.getNickname());
        }
        if (request.getPhone() != null) {
            if (memberRepository.existsByPhoneAndIdNot(request.getPhone(), memberId)) {
                throw new BusinessException(ErrorCode.DUPLICATE_PHONE);
            }
            member.setPhone(request.getPhone());
        }
        if (request.getPassword() != null) {
            member.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return memberMapper.toResponse(member);
    }

    // TODO: 고수 프로필 등록 여부를 확인한 뒤 본인의 CUSTOMER ↔ EXPERT 전환을 연결
    @Transactional
    public MemberResponse setRole(Long adminId, Long memberId, MemberRoleUpdateRequest request) {
        Member member = getMemberEntity(memberId);
        if (adminId.equals(memberId)) {
            throw new BusinessException(ErrorCode.SELF_ROLE_CHANGE_NOT_ALLOWED);
        }

        if (member.getRole() != request.getRole()) {
            member.setRole(request.getRole());
        }
        return memberMapper.toResponse(member);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = getMemberEntity(memberId);
        member.setDeletedAt(LocalDateTime.now());
        member.setNickname("탈퇴한회원_" + memberId);
        member.setEmail("deleted_" + memberId + "@deleted.local");
        member.setPhone(null);
        member.setPassword(null);
        invalidateTokens(member);
    }

    public Member getMemberEntity(Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private void invalidateTokens(Member member) {
        refreshTokenRepository.deleteByMemberId(member.getId());
        member.setTokenInValidBefore(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusSeconds(1));
    }

    private void ensureNotReserved(String email, String nickname) {
        if ((email != null && email.endsWith("@deleted.local"))
                || (nickname != null && nickname.startsWith("탈퇴한회원_"))) {
            throw new BusinessException(ErrorCode.RESERVED_MEMBER_VALUE);
        }
    }
}
