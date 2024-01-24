package com.shinny.projectboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// @EnableWebSecurity 해당 어노테이션은 SpringBoot 에서 사용할 때 auto-config 에 등록되어 있다.
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .authorizeHttpRequests(auth->auth.anyRequest().permitAll())
                .formLogin().and()
                .build();
    }
}
