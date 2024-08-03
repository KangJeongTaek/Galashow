package com.galashow.galashow.Repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.galashow.galashow.Entity.Member;



@Repository
public interface MemberRepository extends JpaRepository<Member,Long>{
    boolean existsByMemberId(String memberId);

    Optional<Member> findByMemberId(String memberId);
}
