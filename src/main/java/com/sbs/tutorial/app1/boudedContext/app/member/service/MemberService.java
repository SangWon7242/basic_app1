package com.sbs.tutorial.app1.boudedContext.app.member.service;

import com.sbs.tutorial.app1.boudedContext.app.member.entity.Member;
import com.sbs.tutorial.app1.boudedContext.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
  @Value("${custom.genFileDirPath}")
  private String genFileDirPath;
  private  final MemberRepository memberRepository;

  public Member getMemberByUsername(String username) {
    return memberRepository.findByUsername(username).orElse(null);
  }

  public Member join(String username, String password, String email, MultipartFile profileImg) {
    // 프로필 이미지가 저장될 경로
    String profileImgRelPath = "member/" + UUID.randomUUID().toString() + ".png";
    File profileImgFile = new File(genFileDirPath + "/" + profileImgRelPath);
    // c:/spring-temp/app1/member/1234.png

    if(!profileImgFile.canExecute()) {
      profileImgFile.mkdirs();
    }

    try {
      profileImg.transferTo(profileImgFile);
    } catch (Exception e) {
      e.printStackTrace();
    }

    Member member = Member.builder()
        .username(username)
        .password(password)
        .email(email)
        .profileImg(profileImgRelPath)
        .build();

    memberRepository.save(member);
    return member;
  }

  public Member getMemberById(Long id) {
    return memberRepository.findById(id).orElse(null);
  }
}
