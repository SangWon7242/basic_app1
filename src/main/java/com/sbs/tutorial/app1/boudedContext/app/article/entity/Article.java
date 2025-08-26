package com.sbs.tutorial.app1.boudedContext.app.article.entity;

import com.sbs.tutorial.app1.base.entity.BaseEntity;
import com.sbs.tutorial.app1.boudedContext.app.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Article extends BaseEntity {
  @ManyToOne
  private Member author;
  private String title;
  private String content;
}
