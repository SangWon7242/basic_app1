package com.sbs.tutorial.app1.boudedContext.app.article.service;

import com.sbs.tutorial.app1.boudedContext.app.article.entity.Article;
import com.sbs.tutorial.app1.boudedContext.app.article.repository.ArticleRepository;
import com.sbs.tutorial.app1.boudedContext.app.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
  private final ArticleRepository articleRepository;

  public Article write(Long authorId, String title, String content) {
    Article article = Article
        .builder()
        .author(new Member(authorId))
        .title(title)
        .content(content)
        .build();

    articleRepository.save(article);

    return article;
  }
}
