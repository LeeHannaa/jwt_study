package com.example.springjwt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTPartsParser;
import com.example.springjwt.auth.PrincipalDetails;
import com.example.springjwt.entity.User;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.naming.AuthenticationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Date;

// /login을 요청하면 username, password를 post로 전송하면
// UsernamePasswordAuthenticationFilter 필터가 작동함. (원래)
// 지금은 filter를 스프링에 직접 등록해줘야함! (UsernamePasswordAuthenticationFilter)

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 자동으로 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("JwtAuthenticationFilter 로그인 시도중");

        // 1. username, password를 받아서
        try{
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            // formLogin하면 해주는 것인데 지금은 직접 해줘야함.
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            // => PrincipalDetailsService의 loadUserByUsername() 실행
            // 함수 실행 후 정상이면 authentication을 리턴
            // DB에 있는 username과 password가 일치
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 완료 : " + principalDetails.getUser().getUsername()); // -> 로그인이 되었다는 뜻

            // authentication 객체가 session 영역에 저장 -> return 해주면 됨
            // return의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는 것
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 2. 정상인지 로그인 시도를 해보기 -> authenticationManager로 로그인 시도를 하면
        // => PrincipalDetailsService가 자동으로 호출됨! => UserDetailsService 실행
        // => 정상적으로 PrincipalDetails가 return 되면 이것을 세션에 담고 (세션에 -> 권한 관리 해주기 위함)
        // jwt 토큰을 만들어서 응답해주기.

    }

    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행
    // => jwt 토큰을 만들어서 request 요청한 사용자에게 jwt 토큰을 response 해주면 됨.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authResult) throws IOException, ServletException{
        System.out.println("successfulAuthentication - 인증완료");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // RSA(공개키/개인키) 방식은 아니고 Hash 암호화 방식
        String jwtToken = JWT.create()
                .withSubject("cos 토큰")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000)*10))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("cos")); // 서버만 알고 있는 시크릿 값 : cos

        response.addHeader("Authorization", "Bearer " + jwtToken);
    }
}
