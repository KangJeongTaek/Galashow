package com.galashow.galashow.Entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mid;

    @Column(unique = true,length = 100)
    private String memberId;

    @Column(length = 100)
    private String password;

    @Column(length = 100)
    private String nickName;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @CreatedDate
    @Column(name ="joinDate", updatable = false)
    private LocalDateTime joinDate; //회원가입일
}
