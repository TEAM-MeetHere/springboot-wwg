package com.example.wherewego.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;
import javax.sql.DataSource;


@Configuration
@EnableWebSecurity //Spring 이 글로벌 웹 security 에 클래스를 찾아 자동으로 적용할 수 있도록 함
@EnableGlobalMethodSecurity(prePostEnabled = true) //@PreAuthorize 어노테이션을 메소드 단위로 추가하기 위해 사용
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource);
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring()
                .antMatchers(
                        "/h2-console/**",
                        "/favicon.ico"
                );
    }


    //WebSecurityConfigurerAdapter 인터페이스에서 configure(HttpSecurity http)메서드를 재정의
    //Spring Security에 CORS 및 CSRF를 구성하는 방법, 모든 사용자의 인증을 요구할지 여부, 필터(AuthTokenFilter) 및
    //작동(UsernamePasswordAuthenticationFilter 전에 필터링), 어떤 예외 핸들러가 선택되었는지 (AuthEntryPointJwt) 알려줌
    @Override
    protected void configure(HttpSecurity http) throws Exception {
     /*   http.cors().disable()
                .csrf().disable()
                .formLogin().disable()
                .headers().frameOptions().disable();*/

        http.csrf().disable() //토큰을 사용하기 위해
                .authorizeRequests().antMatchers(
                "/authenticate",
                "/api/members",
                "/api/members/verify",
                "/api/members/findId",
                "/api/members/findPw").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                //Spring Security에서 Session을 생성하거나 사용하지 않는다.
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
