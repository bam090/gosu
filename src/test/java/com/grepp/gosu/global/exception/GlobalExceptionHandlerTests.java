package com.grepp.gosu.global.exception;

import com.grepp.gosu.global.security.RefreshTokenCookie;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// ErrorCode + BusinessException을 던지면 어떤 응답이 나가는지 보여주는 테스트
// 실제 도메인 없이 테스트 전용 컨트롤러로 확인한다
class GlobalExceptionHandlerTests {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler(new RefreshTokenCookie(1209600000L)))
                .build();
    }

    @Test
    void BusinessException을_던지면_ErrorCode의_상태코드와_메시지로_응답한다() throws Exception {
        mockMvc.perform(get("/test/members/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다."))
                .andExpect(jsonPath("$.errors").doesNotExist()); // errors가 비면 응답에서 빠진다
    }

    @Test
    void 같은_ErrorCode를_다른_곳에서_던져도_같은_응답이_나간다() throws Exception {
        mockMvc.perform(post("/test/bookmarks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다."));
    }

    @Test
    void 리프레시_토큰_오류면_쿠키를_지우는_헤더도_내려간다() throws Exception {
        mockMvc.perform(post("/test/refresh"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string(HttpHeaders.SET_COOKIE,
                        org.hamcrest.Matchers.containsString("Max-Age=0")));
    }

    @Test
    void Valid_검증에_실패하면_필드별_메시지를_errors에_담는다() throws Exception {
        mockMvc.perform(post("/test/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("입력값이 올바르지 않습니다."))
                .andExpect(jsonPath("$.errors.email").value("이메일은 필수입니다."));
    }

    @Test
    void 경로_변수_타입이_다르면_400으로_응답한다() throws Exception {
        mockMvc.perform(get("/test/members/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("경로 또는 쿼리 값의 타입이 올바르지 않습니다."));
    }

    // 테스트 전용 컨트롤러: 서비스에서 예외를 던지는 상황을 흉내 낸다
    @RestController
    @RequestMapping("/test")
    public static class TestController {

        // MemberService에서 회원이 없을 때
        @GetMapping("/members/{memberId}")
        public String getMember(@PathVariable Long memberId) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION);
        }

        // BookmarkService에서 같은 ErrorCode를 재사용할 때
        @PostMapping("/bookmarks/{memberId}")
        public String bookmark(@PathVariable Long memberId) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION);
        }

        // AuthService에서 리프레시 토큰이 잘못됐을 때
        @PostMapping("/refresh")
        public String refresh() {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN_EXCEPTION);
        }

        @PostMapping("/signup")
        public String signup(@Valid @RequestBody SignupRequest request) {
            return "ok";
        }
    }

    public record SignupRequest(@NotBlank(message = "이메일은 필수입니다.") String email) {
    }
}
