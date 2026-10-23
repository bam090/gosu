package com.grepp.gosu.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// BaseEntity의 생성일, 수정일 자동 기록을 켠다
// Application 클래스에 붙이지 않고 따로 둔다 (@WebMvcTest에서 JPA 관련 오류가 나지 않도록)
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
