package com.galashow.galashow.Security.jwt.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.galashow.galashow.Security.jwt.constants.JwtConstants;
import com.galashow.galashow.Security.jwt.provider.JwtTokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter{

    private final JwtTokenProvider jwtTokenProvider;


    // jwt 요청 필터
    //  request -> headers > Authorization (JWT)
    //  토큰 유효성 검사
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(JwtConstants.TOKEN_HEADER);
        log.info("request = {}",request);
        log.info("Authorization = {}", header);

        // Bearer + jwt 체크
        // 토큰이 없으면 다음 필터로 이동
        if(header == null || header.length() == 0 || !header.startsWith(JwtConstants.TOEKEN_PREFIEX)){
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰이 존재한다는 이야기
        
        String jwt = header.replace(JwtConstants.TOEKEN_PREFIEX, "");
        log.info(jwt);
        //토큰 해석
        Authentication authentication = jwtTokenProvider.getAuthentication(jwt);

        // 토큰 유효성 검사
        if(jwtTokenProvider.validateToken(jwt)){
            log.info("authentication123 ={}",authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 다음 필터
        filterChain.doFilter(request, response);
    }
    
}
