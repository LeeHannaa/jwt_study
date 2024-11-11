package com.example.springjwt.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class FilterTest implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException{

        HttpServletRequest req= (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 토큰을 만들었다는 가정 하에 'cors' -> 토큰이 일치하면 컨트롤러로 진입 가능하도록 구현
        if(req.getMethod().equals("POST")){
            System.out.println("post 요청 됨!!");
            String headerAuth = req.getHeader("Authorizaion");
            System.out.println("headerAuth : " + headerAuth);
            if(headerAuth.equals("cors")) filterChain.doFilter(req, res); // filter 작동 후 다시 서비스 실행 -> chain에 다시 넘겨줘야 함.
            else {
                PrintWriter out = res.getWriter();
                out.println("인증안됨! 토큰 불일치!");
            }
        }

        System.out.println("jwtFilter");
    }

}
