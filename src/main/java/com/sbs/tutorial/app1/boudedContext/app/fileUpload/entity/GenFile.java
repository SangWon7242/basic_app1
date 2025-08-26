package com.sbs.tutorial.app1.boudedContext.app.fileUpload.entity;

import com.sbs.tutorial.app1.base.entity.BaseEntity;
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
public class GenFile extends BaseEntity {
  private String relTypeCode;
  private Long relId;
  private String typeCode;
  private String type2Code;
  private String fileExtTypeCode;
  private String fileExtType2Code;
  private int fileSize;
  private int fileNo;
  private String fileExt;
  private String fileDir;
  private String originFileName;
}