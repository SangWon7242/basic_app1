package com.sbs.tutorial.app1.boudedContext.app.security.dto;

import com.sbs.tutorial.app1.boudedContext.app.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class MemberContext extends User implements OAuth2User {
  private final Long id;
  private final String profileImgUrl;
  private final String email;
  private Map<String, Object> attributes;
  private String userNameAttributeName;

  public MemberContext(Member member, List<GrantedAuthority> authorities) {
    super(member.getUsername(), member.getPassword(), authorities);
    this.id = member.getId();
    this.email = member.getEmail();
    this.profileImgUrl = member.getProfileImgUrl();
  }

  public MemberContext(Member member, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String userNameAttributeName) {
    super(member.getUsername(), member.getPassword(), authorities);
    this.id = member.getId();
    this.email = member.getEmail();
    this.profileImgUrl = member.getProfileImgUrl();
    this.attributes = attributes;
    this.userNameAttributeName = userNameAttributeName;
  }

  // 사용자가 가지고 있는 권한 목록을 반환
  @Override
  public Set<GrantedAuthority> getAuthorities() {
    return super.getAuthorities().stream().collect(Collectors.toSet());
  }
  
  // OAuth2User 원본 데이터
  // 모든 사용자 정보를 Map 형태로 반환
  @Override
  public Map<String, Object> getAttributes() {
    return this.attributes;
  }
  
  // 사용자의 고유 식별 번호를 반환
  @Override
  public String getName() {
    return this.getAttribute(this.userNameAttributeName).toString();
  }

  public String profileImgRedirectUrl() {
    return "/member/profile/img/" + getId() + "?random=" + UUID.randomUUID();
  }
}
