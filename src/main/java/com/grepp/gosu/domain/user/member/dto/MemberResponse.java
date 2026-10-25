package com.grepp.gosu.domain.user.member.dto;

import com.grepp.gosu.domain.user.member.MemberRole;

public record MemberResponse(
        Long id,
        String email,
        String nickname,
        String phone,
        MemberRole role
) {}
