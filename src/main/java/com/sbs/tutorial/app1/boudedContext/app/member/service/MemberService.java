package com.sbs.tutorial.app1.boudedContext.app.member.service;

import com.sbs.tutorial.app1.boudedContext.app.member.entity.Member;
import com.sbs.tutorial.app1.boudedContext.app.member.repository.MemberRepository;
import com.sbs.tutorial.app1.util.Util.Util;
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



  public String getCurrentProfileImgDirName() {
    return "member/" + Util.date.getCurrentDateFormatted("yyyy_MM_dd");
  }

  private String saveProfileImg(MultipartFile profileImg) {
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

    return profileImgDirName + "/" + fileName;
  }

  public Member join(String username, String password, String email, MultipartFile profileImg) {
    String profileImgRelPath = saveProfileImg(profileImg);

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

  public void modify(Member member, MultipartFile profileImg) {

    if(profileImg != null && !profileImg.isEmpty()) {
      removeProfileImg(member);
      String profileImgRelPath = saveProfileImg(profileImg);
      member.setProfileImg(profileImgRelPath);
    };

    memberRepository.save(member);
  }

  public void deleteProfileImg(Member member) {
    removeProfileImg(member); // 로컬에서 프로필 이미지 제거
    member.setProfileImg(null); // DB에서 프로필 이미지 제거
    memberRepository.save(member);
  }
}
