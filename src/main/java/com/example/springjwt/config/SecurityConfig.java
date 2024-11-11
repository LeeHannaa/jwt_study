package com.example.springjwt.config;

import com.example.springjwt.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(httpBasic -> httpBasic.disable())// REST API는 UI를 사용하지 않으므로 기본설정을 비활성화
                .csrf(AbstractHttpConfigurer::disable) //CSRF 보호 기능이 비활성화되어, CSRF 토큰을 체크하지 않음
                .addFilter(corsFilter) // 무조건 이 필터를 탐, Cosr Origin 정책을 따르지 않음
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // jwt 사용시 session 사용 x
                .formLogin((formLogin) -> formLogin
                        .disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "ADMIN", "MANAGER")
                        .requestMatchers("/api/v1/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll());
        return httpSecurity.build();
    }
}
