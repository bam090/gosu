package com.grepp.gosu.global.config;

import com.grepp.gosu.global.security.CustomAccessDeniedHandler;
import com.grepp.gosu.global.security.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // 로그인 없이 접근할 수 있는 경로
    private static final String[] PUBLIC_URLS = {
            "/api/public/**",                    // 회원가입, 로그인, 재발급, 공개 조회
            "/h2-console/**",                    // 로컬 H2 콘솔
            "/swagger-ui/**", "/v3/api-docs/**", // Swagger (배포에서는 properties로 꺼져 있음)
            "/ws/**",                            // 웹소켓 연결 (인증은 STOMP CONNECT 단계에서 한다)
            "/error"                             // 스프링 기본 에러 경로
    };

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler
    ) throws Exception {
        http
                // URL 권한 규칙: 위에서부터 순서대로 검사하고, 처음 일치한 규칙을 적용한다
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_URLS).permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // JWT를 헤더로 보내는 REST API라서 CSRF 보호를 끈다
                .csrf(AbstractHttpConfigurer::disable)
                // H2 콘솔 화면이 iframe으로 열리도록 같은 출처의 프레임을 허용한다
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                // 세션을 만들지 않는다 (요청마다 토큰으로 인증한다)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 401, 403 응답을 ErrorResponse 형식으로 통일한다
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                );

        return http.build();
    }

    // 회원가입할 때 비밀번호를 암호화하고, 로그인할 때 비교하는 데 쓴다
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
