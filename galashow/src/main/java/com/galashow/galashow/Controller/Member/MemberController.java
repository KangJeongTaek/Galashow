package com.galashow.galashow.Controller.Member;


import org.springframework.web.bind.annotation.RequestMapping;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RestController;

import com.galashow.galashow.Dto.CustomUser;
import com.galashow.galashow.Dto.MemberDto;
import com.galashow.galashow.Entity.Authority;
import com.galashow.galashow.Entity.Member;
import com.galashow.galashow.Service.MemberService;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;







@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/info")
    public ResponseEntity<?> userInfo(@AuthenticationPrincipal CustomUser customUser) {
        log.info("customUser = {}",customUser.getMember());
        Member member = customUser.getMember();
        if(member != null){
            return new ResponseEntity<>(member,HttpStatus.OK);
        }
        return new ResponseEntity<>("UNAUTHORIZED",HttpStatus.UNAUTHORIZED);
    }
    
    // TODO: 20240803 마무리 영상 URL 및 위치 : https://www.youtube.com/watch?v=ewOSi1NcRLE&list=PL4C2AmBC9jOZSDU5D2kNWNaMv6IW3U4ll&index=9 14 분 24초
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody MemberDto memberDto) {
        Member member = Member.builder()
                            .authority(Authority.ROLE_USER)
                            .joinDate(LocalDateTime.now()
                            ).memberId(memberDto.getMemberId())
                            .password(passwordEncoder.encode(memberDto.getPassword()))
                            .nickName(memberDto.getNickName())
                            .build();
                        
        memberService.saveMember(member);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    
    
}
