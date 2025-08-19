package com.sbs.tutorial.app1.base;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.ignoringRequestMatchers("/**")) // csrf 허용
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/member/join").permitAll()
            .anyRequest().permitAll() // 모든 요청 허용
            
        ).formLogin(form -> form
            .loginPage("/member/login") // GET : 로그인 페이지
            .loginProcessingUrl("/member/login") // POST : 로그인 처리
            .defaultSuccessUrl("/member/profile") // 로그인 성공시 리다이렉트
            .permitAll()
        );

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // 암호화
  }

  @Bean
  // 스프링 시큐리티 인증을 처리
  // 커스텀 인증 로직을 구현할 때 필요
  // 인증의 핵심 관리자
  AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
