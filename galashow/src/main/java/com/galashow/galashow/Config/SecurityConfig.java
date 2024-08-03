package com.galashow.galashow.Config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.galashow.galashow.Security.CustomUserDetailService;
import com.galashow.galashow.Security.jwt.filter.JwtAuthenticationFilter;
import com.galashow.galashow.Security.jwt.filter.JwtRequestFilter;
import com.galashow.galashow.Security.jwt.provider.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

  

    private final JwtTokenProvider jwtTokenProvider;

    private final CustomUserDetailService customUserDetailService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,AuthenticationManager authenticationManager) throws Exception{
        // 폼 기반 로그인 비활성화
        http.formLogin(formLogin -> formLogin.disable());
        //  HTTP 기본 인증 비활성화
        http.httpBasic((basic) -> basic.disable());
        // CSRF 공격 방어 기능 비활성화
        http.csrf(crsf -> crsf.disable());

        // 필터 설정
        http.addFilterAt(new JwtAuthenticationFilter(authenticationManager, jwtTokenProvider),UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new JwtRequestFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        // 인가 설정
        http.authorizeHttpRequests((atr) ->
                                    atr.requestMatchers("/").permitAll()
                                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                        .requestMatchers("/board/**").permitAll()
                                        .requestMatchers("/login").permitAll()
                                        .requestMatchers("/member/signUp").permitAll()
                                        .requestMatchers("/user/**").hasAnyRole("USER","ADMIN")
                                        .requestMatchers("/admin/**").hasRole("ADMIN")
                                        .anyRequest().authenticated())
                                        ;    

        // 세션 관리 정책 설정 비활성화
        // 세션 인증을 사용하지 않고 ,JWT를 사용하여 인증하기 때문에, 세션 불필요
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 인증 방식 설정
        http.userDetailsService(customUserDetailService);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:3000"));
        config.setAllowCredentials(true);
        config.setAllowedMethods(Arrays.asList("HEAD","GET","POST","PATCH","DELETE","OPTIONS","PUT"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    };


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}

