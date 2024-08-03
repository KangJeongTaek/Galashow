package com.galashow.galashow.Dto;

import java.time.LocalDateTime;

import com.galashow.galashow.Entity.Authority;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberDto {
    

    private Long mid;

    private String memberId;

    private String password;

    private String nickName;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private LocalDateTime joinDate;
}
