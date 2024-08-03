package com.galashow.galashow.Security;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.galashow.galashow.Dto.CustomUser;
import com.galashow.galashow.Entity.Member;
import com.galashow.galashow.Repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService{

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Optional<Member> member = memberRepository.findByMemberId(username);
            if(member.isEmpty()){
                log.info("사용자 없음");
                throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
            }

            log.info(member.get().getMemberId());

            // member -> CustomUser 변환
            CustomUser customUser = new CustomUser(member.get());
            log.info("customUser={}",customUser.getUsername());
        return customUser;
    }
    
}
