package com.sbs.tutorial.app1.boudedContext.app.article.input;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleForm {
  @NotEmpty(message = "제목을 입력해주세요.")
  private String title;

  @NotEmpty(message = "내용을 입력해주세요.")
  private String content;
}
