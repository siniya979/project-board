package com.shinny.projectboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing // JpaAuditing 기능 사용을 위함.
@Configuration // 각종 설정을 위해 configuration bean 으로 관리
public class JpaConfig {

    // Article 클래스에서 정의된 @CreatedBy, @LastModifiedBy 에 대해 JpaConfig 에서 설정.
    @Bean
    public AuditorAware<String> auditorAware(){
        return ()-> Optional.of("알 수 없음");   // TODO : 스프릥 시큐리티로 인증기능을 붙일 때, 수정.
    }

}
