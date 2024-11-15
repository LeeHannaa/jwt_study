package com.example.springjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        // cors 정책에서 벗어날 수 있음
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 서버가 응답할 때 json을 자바스크립트에서 처리 가능하게 할 지 설정 (false -> 자바스크립트로 요청했을 때 응답이 오지 않음)
        config.addAllowedOrigin("*"); // 모든 ip에 응답 허용
        config.addAllowedHeader("*"); // 모든 header에 응답 허용
        config.addAllowedMethod("*"); // 모든 post, get, put, delete, patch 요청을 허용
        source.registerCorsConfiguration("/api/**", config); // '/api/'로 된 주소는 모두 config 필터를 거치도록 한다.

        return new CorsFilter(source);
    }
}
