package com.grepp.gosu.domain.user.member;

import com.grepp.gosu.domain.user.member.dto.MemberPublicResponse;
import com.grepp.gosu.domain.user.member.dto.MemberResponse;
import com.grepp.gosu.domain.user.member.dto.MemberRoleUpdateRequest;
import com.grepp.gosu.domain.user.member.dto.MemberUpdateRequest;
import com.grepp.gosu.global.common.PageResponse;
import com.grepp.gosu.global.security.CustomUserDetails;
import com.grepp.gosu.global.security.RefreshTokenCookie;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원", description = "회원 정보 조회·수정·탈퇴 및 관리자 회원 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;
    private final RefreshTokenCookie refreshTokenCookie;

    @Operation(summary = "회원 목록 조회")
    @GetMapping("/admin/members")
    public ResponseEntity<PageResponse<MemberResponse>> getMembers(
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        PageResponse<MemberResponse> response = memberService.getMembers(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 정보 조회")
    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> getMyData(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberResponse response = memberService.getCurrentMember(userDetails.getMemberId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 공개 정보 조회")
    @SecurityRequirements
    @GetMapping("/public/members/{memberId}")
    public ResponseEntity<MemberPublicResponse> getMember(@PathVariable Long memberId) {
        MemberPublicResponse response = memberService.getMemberById(memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 정보 수정")
    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMember(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody MemberUpdateRequest request
    ) {
        MemberResponse response = memberService.updateMember(userDetails.getMemberId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 권한 변경")
    @PatchMapping("/admin/members/{memberId}/role")
    public ResponseEntity<MemberResponse> setRole(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long memberId,
            @Valid @RequestBody MemberRoleUpdateRequest request
    ) {
        MemberResponse response = memberService.setRole(userDetails.getMemberId(), memberId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/members/me")
    public ResponseEntity<Void> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.deleteMember(userDetails.getMemberId());
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.expire().toString()).build();
    }

    @Operation(summary = "관리자 회원 삭제")
    @DeleteMapping("/admin/members/{memberId}")
    public ResponseEntity<Void> deleteMemberByAdmin(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
