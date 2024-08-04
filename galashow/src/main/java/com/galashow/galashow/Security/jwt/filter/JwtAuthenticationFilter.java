package com.galashow.galashow.Security.jwt.filter;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.galashow.galashow.Dto.CustomUser;
import com.galashow.galashow.Security.jwt.constants.JwtConstants;
import com.galashow.galashow.Security.jwt.provider.JwtTokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider){
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        setFilterProcessesUrl(JwtConstants.AUTH_LOGIN_URL);
    }
    // 인증 시도 메소드
    // /login 경로로 요청하면, 필터로 걸러서 인증을 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String memberId = request.getParameter("memberId");
        String password = request.getParameter("password");
        log.info("/login 경로로 요청 = {}","요청");
        log.info("memberId = {}",memberId);

        Authentication authentication = new UsernamePasswordAuthenticationToken(memberId, password);

        authentication = authenticationManager.authenticate(authentication);

        if(!authentication.isAuthenticated()){
            response.setStatus(401); // UNAUTHORIZED(인증 실패)
        }
        return authentication;

    }


    // 인증 성공 메소드
    // JWT 을 생성
    // JWT 을 응답 헤더에 설정
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        log.info("인증 성공...");
        CustomUser user = (CustomUser)authResult.getPrincipal();
        Long mid = user.getMember().getMid();
        String memberId = user.getMember().getMemberId();
        String role = user.getMember().getAuthority().toString();
        String nickName = user.getMember().getNickName();

        String jwt = jwtTokenProvider.createToken(mid, memberId, role, nickName);

        response.addHeader(JwtConstants.TOKEN_HEADER,JwtConstants.TOEKEN_PREFIEX + jwt);
        response.setStatus(200);
    }
}
