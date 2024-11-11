package com.example.springjwt.config;

import com.example.springjwt.filter.FilterTest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<FilterTest> filter(){ // security filter가 아니라 따로 filter 걸어주기
        FilterRegistrationBean<FilterTest> filterBean = new FilterRegistrationBean<>(new FilterTest());
        filterBean.addUrlPatterns("/*");
        filterBean.setOrder(0); // 낮은 번호가 우선 실행
        return filterBean;
    }
}
