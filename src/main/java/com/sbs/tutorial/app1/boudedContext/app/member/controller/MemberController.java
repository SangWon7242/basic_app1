package com.sbs.tutorial.app1.boudedContext.app.member.controller;

import com.sbs.tutorial.app1.boudedContext.app.member.entity.Member;
import com.sbs.tutorial.app1.boudedContext.app.member.service.MemberService;
import com.sbs.tutorial.app1.boudedContext.app.security.dto.MemberContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.midi.MetaMessage;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.concurrent.TimeUnit;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
  private final MemberService memberService;
  private final PasswordEncoder passwordEncoder;

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/modify")
  public String showModify() {
    return "member/modify";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/modify")
  public String modify(@AuthenticationPrincipal MemberContext memberContext, String email, String profileImgDelete, MultipartFile profileImg) {
    Member member = memberService.getMemberById(memberContext.getId());

    if(profileImgDelete != null && profileImgDelete.equals("Y")) {
      memberService.removeProfileImg(member);
    }

    memberService.modify(member, email, profileImg);

    return "redirect:/member/profile";
  }


  @PreAuthorize("isAnonymous()") // 로그인 하지 않은 회원만 접속 가능
  @GetMapping("/join")
  public String showJoin() {
    return "member/join";
  }

  @PreAuthorize("isAnonymous()")
  @PostMapping("/join")
  public String join(String username, String password, String email, MultipartFile profileImg, HttpServletRequest req) {
    Member oldMember = memberService.getMemberByUsername(username);

    String passwordClearText = password; // 패스워드 원문
    password = passwordEncoder.encode(password);

    if (oldMember != null) {
      return "redirect:/?errorMsg=AlreadyExist";
    }

    Member member = memberService.join(username, password, email, profileImg);

    try {
      req.login(username, passwordClearText); // 로그인 처리
    } catch (ServletException e) {
      throw new RuntimeException(e);
    }

    return "redirect:/member/profile";
  }

  @PreAuthorize("isAnonymous()")
  @GetMapping("/login")
  public String showLogin() {
    return "member/login";
  }

  @PreAuthorize("isAuthenticated()") // 로그인 회원만 접속이 가능
  @GetMapping("/profile")
  public String showProfile() {
    return "member/profile";
  }

  @GetMapping("/principal")
  @ResponseBody
  public Principal getPrincipal(Principal principal) {
    return principal;
  }

  @GetMapping("/currentUser")
  @ResponseBody
  public MemberContext currentUser(@AuthenticationPrincipal MemberContext memberContext) {
    return memberContext;
  }

//  @GetMapping("/profile/img/{id}")
//  public String showProfileImgOrigin(@PathVariable("id") Long id) {
//    return "redirect:" + memberService.getMemberById(id).getProfileImgUrl();
//  }

  @GetMapping("/profile/img/{id}")
  public ResponseEntity<Object> showProfileImg(@PathVariable Long id) throws URISyntaxException {
    URI redirectUri = new URI(memberService.getMemberById(id).getProfileImgUrl());
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(redirectUri);
    httpHeaders.setCacheControl(CacheControl.maxAge(60 * 60 * 1, TimeUnit.SECONDS));
    return new ResponseEntity<>(httpHeaders, HttpStatus.FOUND);
  }
}
