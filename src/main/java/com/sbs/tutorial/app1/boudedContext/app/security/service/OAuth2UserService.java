package com.sbs.tutorial.app1.boudedContext.app.security.service;

import com.sbs.tutorial.app1.boudedContext.app.member.entity.Member;
import com.sbs.tutorial.app1.boudedContext.app.member.exception.MemberNotFoundException;
import com.sbs.tutorial.app1.boudedContext.app.member.repository.MemberRepository;
import com.sbs.tutorial.app1.boudedContext.app.member.service.MemberService;
import com.sbs.tutorial.app1.boudedContext.app.security.dto.MemberContext;
import com.sbs.tutorial.app1.boudedContext.app.security.exception.OAuthTypeMatchNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
  private final MemberRepository memberRepository;
  private final MemberService memberService;

  @Override
  @Transactional
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);

    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
        .getUserNameAttributeName();
    Map<String, Object> attributes = oAuth2User.getAttributes();

    String oauthId = oAuth2User.getName();

    Member member = null;
    String oauthType = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

    if (!"KAKAO".equals(oauthType)) {
      throw new OAuthTypeMatchNotFoundException();
    }

    if (isNew(oauthType, oauthId)) {
      switch (oauthType) {
        case "KAKAO" -> {
          Map attributesProperties = (Map) attributes.get("properties");
          Map attributesKakaoAcount = (Map) attributes.get("kakao_account");
          String nickname = (String) attributesProperties.get("nickname");
          String email = "%s@kakao.com".formatted(oauthId);
          String username = "KAKAO_%s".formatted(oauthId);

          if ((boolean) attributesKakaoAcount.get("has_email")) {
            email = (String) attributesKakaoAcount.get("email");
          }

          member = Member.builder()
              .email(email)
              .username(username)
              .password("")
              .build();

          memberRepository.save(member);

          memberService.setProfileImgByUrl(member, "https://picsum.photos/200/300");
        }
      }
    } else {
      member = memberRepository.findByUsername("%s_%s".formatted(oauthType, oauthId))
          .orElseThrow(MemberNotFoundException::new);
    }

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("MEMBER"));
    return new MemberContext(member, authorities, attributes, userNameAttributeName);
  }

  // 소셜로그인을 통한 회원이 존재하지 않으면 회원 가입
  private boolean isNew(String oAuthType, String oAuthId) {
    return memberRepository.findByUsername("%s_%s".formatted(oAuthType, oAuthId)).isEmpty();
  }
}
