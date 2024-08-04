package com.galashow.galashow.Service;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;


import com.galashow.galashow.Entity.Member;
import com.galashow.galashow.Repository.MemberRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;


    public Member saveMember(Member member){
        if(memberRepository.existsByMemberId(member.getNickName())){
            throw new Error("이미 존재하는 유저입니다.");
        }
        return memberRepository.save(member);
        
    }

    public void login(Member member,HttpServletRequest request) throws Exception{
        UsernamePasswordAuthenticationToken token
            = new UsernamePasswordAuthenticationToken(member.getMemberId(), member.getPassword());

        // 토큰에 요청정보 등록
        token.setDetails(new WebAuthenticationDetails(request));

        //토큰을 이용하여 로그인
        Authentication authenticate = authenticationManager.authenticate(token);

        log.info("인 증 여부 :  {}",authenticate.isAuthenticated());
        User authUser = (User) authenticate.getPrincipal();

        log.info("인증된 사용자 아이디 : {}",authUser.getUsername());

        // 시큐리티 컨텍스트에 인증된 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authenticate);
    }
    
    // 회원 수정
    public Member update(Member member) throws Exception{
        String password = member.getPassword();
        String encodePw = passwordEncoder.encode(password);
        member.setPassword(encodePw);
        return memberRepository.save(member);
    }

    // 회원 탈퇴
    public void delete(Member member) throws Exception{
        memberRepository.delete(member);
    }
    
    // 회원 찾기
    public Member findByMemberId(String memberId){
        Optional<Member> member_ = memberRepository.findByMemberId(memberId);
        if(member_.isPresent()){
            return member_.get();
        };
        return null;
    }
}