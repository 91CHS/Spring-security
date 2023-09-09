package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
/*
    구글
   1.코드받기(인증) 2.엑세스토큰(권한)
   3.사용자프로필 정보를 가져오고 4-1.정보를 토대로 회원가입 자동 진행
   4-2.이메일,전화번호,이름,아이디 + 추가정보(주소 등) 필요시 요청
*/
@EnableWebSecurity // 활성화. 스프링 시큐리티 필터(SecurityConfig)가 스프링 기본 필터체인에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize/postAuthorize 어노테이션 활성화
@Configuration
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록된다. 비밀번호 암호화.
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();  // 비활성화
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // 인증만 되면 들어갈 수 있는 주소!!
                // .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
                // .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll() // 위 조건 제외, 다른 요청은 가능.
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 대신 로그인을 진행해준다.
                .defaultSuccessUrl("/") // 로그인이 완료되면 이동함
                .and()
                .oauth2Login()
                .loginPage("/loginForm") // 구글 로그인 후처리가 필요함. Tip. 코드를 받는게 아니라, 엑세스토큰+사용자프로필정보를 받음
                .userInfoEndpoint()
                .userService(principalOauth2UserService);

        return http.build();
    }

}

