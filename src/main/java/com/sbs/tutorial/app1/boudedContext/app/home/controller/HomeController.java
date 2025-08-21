package com.sbs.tutorial.app1.boudedContext.app.home.controller;

import com.sbs.tutorial.app1.boudedContext.app.member.entity.Member;
import com.sbs.tutorial.app1.boudedContext.app.member.service.MemberService;
import com.sbs.tutorial.app1.boudedContext.app.security.dto.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class HomeController {
  private final MemberService memberService;

  @RequestMapping("/")
  public String main(Principal principal, Model model) {
    Member loginedMember = null;
    String loginedMemberProfileImgUrl = null;

    if(principal != null && principal.getName() != null) {
      loginedMember = memberService.getMemberByUsername(principal.getName());
    }

    if(loginedMember != null) {
      loginedMemberProfileImgUrl = "/gen/" + loginedMember.getProfileImg();
    }

    model.addAttribute("loginedMember", loginedMember);
    model.addAttribute("loginedMemberProfileImgUrl", loginedMemberProfileImgUrl);

    return "home/main";
  }

  @RequestMapping("/test/upload")
  public String upload() {
    return "home/test/upload";
  }

}
