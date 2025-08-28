package com.sbs.tutorial.app1.base.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@SuperBuilder
@MappedSuperclass // BaseEntity를 상속받는 모든 자식 클래스에 @Entity, @Table 어노테이션이 자동으로 추가된다.
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@ToString
public class BaseEntity {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  @CreatedDate
  private LocalDateTime createDate; // LocalDateTime.NOW() 가 알아서 들어감
  @LastModifiedDate
  private LocalDateTime modifyDate;

  @Transient // 아래 필드가 DB 필드가 되는 것을 막는다.
  @Builder.Default
  private Map<String, Object> extra = new LinkedHashMap<>();

  public BaseEntity(Long id) {
    this.id = id;
  }
}
