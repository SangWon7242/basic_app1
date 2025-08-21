package com.sbs.tutorial.app1.boudedContext.app.member.service;

import com.sbs.tutorial.app1.boudedContext.app.member.entity.Member;
import com.sbs.tutorial.app1.boudedContext.app.member.repository.MemberRepository;
import com.sbs.tutorial.app1.util.Util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
  @Value("${custom.genFileDirPath}")
  private String genFileDirPath;
  private  final MemberRepository memberRepository;

  public Member getMemberByUsername(String username) {
    return memberRepository.findByUsername(username).orElse(null);
  }



  public String getCurrentProfileImgDirName() {
    return "member/" + Util.date.getCurrentDateFormatted("yyyy_MM_dd");
  }

  public Member join(String username, String password, String email, MultipartFile profileImg) {
    // 프로필 이미지가 저장될 경로
    String profileImgDirName = getCurrentProfileImgDirName();
    String ext = Util.file.getExt(profileImg.getOriginalFilename());
    String fileName = UUID.randomUUID().toString() + "." + ext;
    String profileImgDirPath = genFileDirPath + "/" + profileImgDirName; // 폴더 경로
    String profileImgFilePath = profileImgDirPath + "/" + fileName; // 파일 경로

    new File(profileImgDirPath).mkdirs(); // 폴더 생성

    try {
      profileImg.transferTo(new File(profileImgFilePath)); // 파일 저장
    } catch (Exception e) {
      e.printStackTrace();
    }

    String profileImgRelPath = profileImgDirName + "/" + fileName;
    System.out.println("profileImgRelPath : " + profileImgRelPath);

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

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    
    // 1. 사용자 조회
    Member member = memberRepository.findByUsername(username).orElse(null);
    
    // 2. 권한 설정
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("member"));

    return new User(member.getUsername(), member.getPassword(), authorities);
  }

  public Member join(String username, String password, String email) {
    Member member = Member.builder()
        .username(username)
        .password(password)
        .email(email)
        .build();

    memberRepository.save(member);

    return member;
  }

  public long count() {
    return memberRepository.count();
  }

  public void removeProfileImg(Member member) {
    if(member.getProfileImg() == null || member.getProfileImg().isEmpty()) return;

    String profileImgPath = genFileDirPath + "/" + member.getProfileImg();
    File file = new File(profileImgPath);

    if(file.exists()) file.delete();
  }

  public void setProfileImgByUrl(Member member, String url) {
    Member freshMember = memberRepository.findById(member.getId()).orElseThrow();

    String filePath = Util.file.downloadImg(url, genFileDirPath + "/" + getCurrentProfileImgDirName() + "/" + UUID.randomUUID());
    freshMember.setProfileImg(getCurrentProfileImgDirName() + "/" + new File(filePath).getName());
    memberRepository.save(freshMember);
  }
}
