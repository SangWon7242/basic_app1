package com.sbs.tutorial.app1.boudedContext.app.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sbs.tutorial.app1.base.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {
  @Column(unique = true)
  private String username;

  @JsonIgnore
  private String password;
  private String email;
  private String profileImg;

  public Member(Long id) {
    super(id);
  }

  public String getProfileImgUrl() {
    if (profileImg == null) return null;

    return "/gen/" + profileImg;
  }
}
