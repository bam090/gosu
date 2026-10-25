package com.grepp.gosu.domain.user.member.dto;

import com.grepp.gosu.domain.user.member.MemberRole;

public record MemberPublicResponse(Long id, String nickname, MemberRole role) {}
