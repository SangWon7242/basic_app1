package com.sbs.tutorial.app1.boudedContext.app.article.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sbs.tutorial.app1.boudedContext.app.article.entity.Article;
import com.sbs.tutorial.app1.boudedContext.app.article.entity.QArticle;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sbs.tutorial.app1.boudedContext.app.article.entity.QArticle.article;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Article> getQslArticlesOrderByIdDesc() {
    return jpaQueryFactory
        .selectFrom(article) // select * from article
        .orderBy(article.id.desc()) // order by id desc
        .fetch();
  }
}
