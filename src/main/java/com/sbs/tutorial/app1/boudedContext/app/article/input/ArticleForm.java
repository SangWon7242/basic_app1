package com.sbs.tutorial.app1.boudedContext.app.article.input;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleForm {
  @NotEmpty
  private String title;
  @NotEmpty
  private String content;
}
