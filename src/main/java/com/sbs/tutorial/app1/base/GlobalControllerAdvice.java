package com.sbs.tutorial.app1.base;

import com.sbs.tutorial.app1.boudedContext.app.member.entity.Member;
import com.sbs.tutorial.app1.boudedContext.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice // 템플릿에 들어갈 공통 기능을 담당한다.
@RequiredArgsConstructor
public class GlobalControllerAdvice {
  private final MemberService memberService;

  @ModelAttribute("loginedMember")
  public Member addLoginedMember(Principal principal) {
    if (principal != null && principal.getName() != null) {
      return memberService.getMemberByUsername(principal.getName());
    }
    return null;
  }

  @ModelAttribute("loginedMemberProfileImgUrl")
  public String addLoginedMemberProfileImg(@ModelAttribute("loginedMember") Member loginedMember) {
    if (loginedMember != null && loginedMember.getProfileImg() != null && !loginedMember.getProfileImg().isEmpty()) {
      System.out.println("loginedMember.getProfileImg() : " + loginedMember.getProfileImg());
      return "/gen/" + loginedMember.getProfileImg();
    }
    return "";
  }
}
