package com.sbs.tutorial.app1.boudedContext.app.member.controller;

import com.sbs.tutorial.app1.boudedContext.app.member.entity.Member;
import com.sbs.tutorial.app1.boudedContext.app.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
  private final MemberService memberService;

  @GetMapping("/join")
  public String showJoin() {
    return "member/join";
  }

  @PostMapping("/join")
  public String join(String username, String password, String email, MultipartFile profileImg, HttpSession session) {
    Member oldMember = memberService.getMemberByUsername(username);

    if(oldMember != null) {
      return "redirect:/?errorMsg=AlreadyExist";
    }
    
    // {noop} : 암호화 되지 않은 비밀번호의 경우 noop을 이용하여 저장
    Member member = memberService.join(username, "{noop}" + password, email, profileImg);

    session.setAttribute("loginedMemberId", member.getId());

    return "redirect:/member/profile";
  }

  @GetMapping("/profile")
  public String showProfile(HttpSession session, Model model) {
    Long loginedMemberId = (Long) session.getAttribute("loginedMemberId");

    boolean isLogined = loginedMemberId != null;

    if(!isLogined) {
      return "redirect:/?errorMsg=NeedLogin";
    }

    Member loginedMember = memberService.getMemberById(loginedMemberId);

    model.addAttribute("loginedMember", loginedMember);

    return "member/profile";
  }

}
