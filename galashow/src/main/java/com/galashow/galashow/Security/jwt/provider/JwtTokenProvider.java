package com.galashow.galashow.Security.jwt.provider;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.galashow.galashow.Dto.CustomUser;
import com.galashow.galashow.Entity.Authority;
import com.galashow.galashow.Entity.Member;
import com.galashow.galashow.Prop.JwtProps;
import com.galashow.galashow.Repository.MemberRepository;
import com.galashow.galashow.Security.jwt.constants.JwtConstants;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// JWT 토큰 관련 기능 제공
// 토큰 생성
// 토큰 해석
// 토큰 유효성 검사
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProps jwtProps;
    private final MemberRepository memberRepository;

    public String createToken(Long mid,String memberid,String role,String nickName){

        String jwt = Jwts.builder()
                            .signWith(getShaKey(),Jwts.SIG.HS512) //시그니쳐 사용할 비밀키, 알고리즘 설정
                                .header()                                           // 헤더 설정
                                .add("type", JwtConstants.TOKEN_TYPE)  //type : JWT
                            .and()
                            .expiration(new Date(System.currentTimeMillis() + 1000*60*60*24*5)) // 토근 만료 시간 설정(5일)
                            .claim("memberId", memberid) // PAYLOAD : memberId : memberId
                            .claim("role", role) // PAYLOAD - role : ROLE_USER or ROLE_ADMIN
                            .claim("nickName",nickName)
                            .claim("mid", mid)
                            .compact(); // 최종적으로 토큰 생성
                            return jwt;
    }
        

    public UsernamePasswordAuthenticationToken getAuthentication(String authHeader){
        if(authHeader == null || authHeader.length() == 0){
            return null;
        }
        try{
            String jwt = authHeader.replace(JwtConstants.TOEKEN_PREFIEX, "");

            log.info("jwt = {}",jwt);
            Jws<Claims> parsedToken = Jwts.parser().verifyWith(getShaKey()).build().parseSignedClaims(jwt);
            log.info("parsedToek = {}",parsedToken);
            Long mid = ((Integer) parsedToken.getPayload().get("mid")).longValue();

            String authority = parsedToken.getPayload().get("role").toString();

            String memberId = parsedToken.getPayload().get("memberId").toString();

            String nickName = parsedToken.getPayload().get("nickName").toString();



            Member member = Member.builder().mid(mid).memberId(memberId).nickName(nickName).authority(Authority.valueOf(authority)).build();

            try{
                Optional<Member> member2 = memberRepository.findById(mid);
                if(member2.isPresent()){
                    member.setJoinDate(member2.get().getJoinDate());
                }
            }catch(Exception e){
                log.error(e.getMessage());
                log.error("토큰 유효 -> DB 추가 정보 조회시 에러 발생");
            }
            log.info("현재 저장되기 직전");
            return new UsernamePasswordAuthenticationToken(new CustomUser(member), null,Collections.singleton(new SimpleGrantedAuthority(authority)));
        } catch(ExpiredJwtException exception){
            log.warn("Request to parse expired JWT : {} failed : {}",authHeader,exception.getMessage());
        }catch(UnsupportedJwtException exception){
            log.warn("Request to parse unsupported JWT : {} failed : {}",authHeader,exception.getMessage());
        }catch(MalformedJwtException exception){
            log.warn("Request to parse invalid JWT : {} failed : {}",authHeader,exception.getMessage());

        }catch(IllegalArgumentException exception){
            log.warn("Request to parse empty or null JWT : {} failed : {}",authHeader,exception.getMessage());
        }
        return null;
    }

    // 토큰 유효성 검사
    // 만료 기간이 넘었는지?
    public boolean validateToken(String jwt){
        try {
            Jws<Claims> parsedToken = Jwts.parser().verifyWith(getShaKey()).build().parseSignedClaims(jwt);

            log.info("## 토큰 만료 기간 ### {}",parsedToken.getPayload().getExpiration());
    
            Date exp = parsedToken.getPayload().getExpiration();
    
            //  만료기한과 오늘 날짜 비교
            log.info("true 반환");
            return !exp.before(new Date());
        }catch(ExpiredJwtException e) {
            log.error("Token Expired");
            return false;
        }catch(JwtException e){
            log.error("Token Tampered");
            return false;
        }catch(NullPointerException e){
            log.error("Token is null");
        }catch(Exception e){
            return false;
        }
        log.info("false로 반환 중");
        return false;
 
    }




    private byte[] getSignKey(){
        return jwtProps.getSecretKey().getBytes();
    }

    private SecretKey getShaKey(){
        return Keys.hmacShaKeyFor(getSignKey());
    }
}
