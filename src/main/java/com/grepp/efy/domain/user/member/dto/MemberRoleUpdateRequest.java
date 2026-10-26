package com.grepp.efy.domain.user.member.dto;

import com.grepp.efy.domain.user.member.MemberRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRoleUpdateRequest {

    @NotNull(message = "권한을 선택해 주세요.")
    private MemberRole role;
}
