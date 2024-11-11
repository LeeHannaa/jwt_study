package com.example.springjwt.auth;

import com.example.springjwt.entity.User;
import com.example.springjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

// /login 요청이 왔을 때 UserDetailsService 동작 (스프링 시큐리티 기본적으로 로그인 요청 주소가 /login 임)
// 하지만 formLogin을 disable 했기 때문에 /login에서 동작을 하지 않음
// => 필터를 만들어서 직접 실행시켜줘야함!!
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userReopsitory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userReopsitory.findByUsername(username);
        System.out.println("여기가 확인 부분 : " + user);
        if(user != null) return new PrincipalDetails(user);
        else return null;
    }

}
