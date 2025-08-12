package com.sbs.tutorial.app1.boudedContext.app.member.repository;

import com.sbs.tutorial.app1.boudedContext.app.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByUsername(String username);
}
