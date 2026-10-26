package com.grepp.gosu.domain.user.auth.docs;

import com.grepp.gosu.domain.user.auth.dto.LoginRequest;
import com.grepp.gosu.domain.user.auth.dto.LoginResponse;
import com.grepp.gosu.domain.user.member.dto.MemberCreateRequest;
import com.grepp.gosu.domain.user.member.dto.MemberResponse;
import com.grepp.gosu.global.security.CustomUserDetails;
import com.grepp.gosu.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원·인증", description = "회원가입·이메일 로그인·토큰 재발급·로그아웃 API")
public interface AuthControllerDocs {

    @Operation(summary = "회원가입",
            description = "이메일·비밀번호·닉네임·전화번호로 일반 사용자(USER) 계정을 생성합니다. 이메일은 앞뒤 공백 제거 및 소문자 변환 후 중복을 검사하며 비밀번호는 응답하지 않습니다.")
    @ApiResponse(responseCode = "201", description = "가입 성공. 생성된 회원 정보 반환")
    @ApiResponse(responseCode = "400", description = "입력값 검증 실패 또는 탈퇴 회원용 예약값 사용",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "비밀번호 길이", value = """
                            {
                              "message": "입력값이 올바르지 않습니다.",
                              "errors": {
                                "password": "비밀번호는 4자 이상 20자 이하로 입력해 주세요."
                              }
                            }
                            """),
                    @ExampleObject(name = "전화번호 누락", value = """
                            {
                              "message": "입력값이 올바르지 않습니다.",
                              "errors": {
                                "phone": "전화번호를 입력해 주세요."
                              }
                            }
                            """),
                    @ExampleObject(name = "예약값 사용", value = """
                            {
                              "message": "탈퇴 회원용 이메일이나 닉네임은 사용할 수 없습니다."
                            }
                            """)
            }))
    @ApiResponse(responseCode = "409", description = "이메일·닉네임·전화번호 중복",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "이메일 중복", value = """
                            {
                              "message": "이미 사용 중인 이메일입니다."
                            }
                            """),
                    @ExampleObject(name = "닉네임 중복", value = """
                            {
                              "message": "이미 사용 중인 닉네임입니다."
                            }
                            """),
                    @ExampleObject(name = "전화번호 중복", value = """
                            {
                              "message": "이미 사용 중인 전화번호입니다."
                            }
                            """)
            }))
    @SecurityRequirements
    ResponseEntity<MemberResponse> signup(MemberCreateRequest memberCreateRequest);

    @Operation(summary = "로그인",
            description = "이메일과 비밀번호로 로그인합니다. Access 토큰은 응답 본문으로, Refresh 토큰은 HttpOnly 쿠키로 전달합니다. 이메일이 없거나 비밀번호가 일치하지 않으면 같은 실패 메시지를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공. Access 토큰 반환 및 Refresh 쿠키 발급")
    @ApiResponse(responseCode = "400", description = "이메일·비밀번호 입력값이 유효하지 않음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "이메일 누락", value = """
                            {
                              "message": "입력값이 올바르지 않습니다.",
                              "errors": {
                                "email": "이메일을 입력해 주세요."
                              }
                            }
                            """)
            }))
    @ApiResponse(responseCode = "401", description = "이메일 또는 비밀번호가 올바르지 않음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "로그인 실패", value = """
                            {
                              "message": "이메일 또는 비밀번호가 올바르지 않습니다."
                            }
                            """)
            }))
    @SecurityRequirements
    ResponseEntity<LoginResponse> login(LoginRequest loginRequest);

    @Operation(summary = "토큰 재발급",
            description = "브라우저가 보내는 refreshToken 쿠키로 새 Access 토큰을 발급합니다. 기존 Refresh 토큰은 새 값으로 교체하며 이전 값의 재사용은 거절합니다. Authorization 헤더는 필요하지 않습니다.")
    @ApiResponse(responseCode = "200", description = "재발급 성공. 새 Access 토큰 반환 및 Refresh 쿠키 교체")
    @ApiResponse(responseCode = "401", description = "Refresh 쿠키 누락·만료·재사용 또는 탈퇴 회원의 토큰",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "유효하지 않은 토큰", value = """
                            {
                              "message": "유효하지 않은 리프레시 토큰입니다."
                            }
                            """)
            }))
    @SecurityRequirements
    ResponseEntity<LoginResponse> refresh(String refreshToken);

    @Operation(summary = "로그아웃",
            description = "로그인한 회원의 Refresh 토큰을 모두 삭제하고 Refresh 쿠키를 만료 처리합니다. 로그아웃 전에 발급된 Access 토큰도 무효화합니다.")
    @ApiResponse(responseCode = "204", description = "로그아웃 성공. 응답 본문 없음")
    @ApiResponse(responseCode = "401", description = "인증 정보가 없거나 유효하지 않음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "인증 실패", value = """
                            {
                              "message": "인증에 실패했습니다."
                            }
                            """)
            }))
    @ApiResponse(responseCode = "404", description = "회원이 없거나 탈퇴한 상태",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "회원 없음", value = """
                            {
                              "message": "회원을 찾을 수 없습니다."
                            }
                            """)
            }))
    ResponseEntity<Void> logout(CustomUserDetails userDetails);
}
