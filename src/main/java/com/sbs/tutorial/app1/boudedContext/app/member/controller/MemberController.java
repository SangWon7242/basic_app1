package com.sbs.tutorial.app1.boudedContext.app.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

  @GetMapping("/join")
  public String showJoin() {
    return "member/join";
  }
}
