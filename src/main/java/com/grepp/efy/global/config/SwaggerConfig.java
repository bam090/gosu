package com.grepp.efy.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "EFY API",
                version = "v1",
                description = "일상 서비스 매칭 플랫폼 REST API입니다."
        ),
        security = @SecurityRequirement(name = "accessToken")
)
@SecurityScheme(
        name = "accessToken",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "로그인 응답의 accessToken 값을 그대로 붙여 넣습니다."
)
public class SwaggerConfig {
}
