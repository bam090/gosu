package com.grepp.efy.domain.user.member.dto;

import com.grepp.efy.domain.user.member.MemberRole;

public record MemberPublicResponse(Long id, String nickname, MemberRole role) {}
