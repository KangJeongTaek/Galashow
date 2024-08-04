package com.galashow.galashow.Controller.Member;


import org.springframework.web.bind.annotation.RequestMapping;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RestController;

import com.galashow.galashow.Dto.CustomUser;
import com.galashow.galashow.Dto.MemberDto;
import com.galashow.galashow.Entity.Authority;
import com.galashow.galashow.Entity.Member;
import com.galashow.galashow.Repository.MemberRepository;
import com.galashow.galashow.Service.MemberService;
import java.util.Optional;
import java.time.LocalDateTime;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;





// [GET] - 회원 정보 조회 (ROLE_USER)
// [POST] - 회원가입 ALL
// [PUT]   - 회원정보 수정 (ROLE_USER)
// [DELETE] - 회원탈퇴 (ROLE_USER)


@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @GetMapping("/info")
    @Secured("ROLE_USER")
    public ResponseEntity<?> userInfo(@AuthenticationPrincipal CustomUser customUser) {
        log.info("customUser = {}",customUser.getMember());
        Member member = customUser.getMember();
        if(member != null){
            return new ResponseEntity<>(member,HttpStatus.OK);
        }
        return new ResponseEntity<>("UNAUTHORIZED",HttpStatus.UNAUTHORIZED);
    }
    
    @PostMapping
    public ResponseEntity<?> signUp(@RequestBody MemberDto memberDto) {
        Member member = Member.builder()
                            .authority(Authority.ROLE_USER)
                            .joinDate(LocalDateTime.now()
                            ).memberId(memberDto.getMemberId())
                            .password(passwordEncoder.encode(memberDto.getPassword()))
                            .nickName(memberDto.getNickName())
                            .build();
        try{
            memberService.saveMember(member);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(DataIntegrityViolationException  e){
            if(e.getCause() instanceof ConstraintViolationException){
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getCause();
                if("23505".equals(constraintViolationException.getSQLState())){
                    return new ResponseEntity<>("이미 존재하는 아이디입니다.",HttpStatus.CONFLICT);
                }
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
    }
    
    // 회원 정보 수정
    @Secured("ROLE_USER") // USER 권한 설정
    @PutMapping("/{mid}")
    public ResponseEntity<?> put(@PathVariable(value = "mid") Long mid, @RequestBody MemberDto memberDto,@AuthenticationPrincipal CustomUser customUser) {
        Member member = customUser.getMember();
        log.info("권한 부여 받은 유저 = {}",member.toString());
        if(member.getMid() != mid){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<Member> memberO = memberRepository.findById(mid);
        if(memberO.isPresent()){
            Member member2 = memberO.get();
            member2.setNickName(memberDto.getNickName());
            member2.setPassword(passwordEncoder.encode(memberDto.getPassword()));
            memberRepository.save(member2);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>("회원 정보를 찾을 수 없습니다.",HttpStatus.BAD_REQUEST);
        }
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/{mid}")
    public ResponseEntity<?> delete(@PathVariable("mid")Long mid, @AuthenticationPrincipal CustomUser customUser){
        Member member = customUser.getMember();
        memberRepository.delete(member);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
