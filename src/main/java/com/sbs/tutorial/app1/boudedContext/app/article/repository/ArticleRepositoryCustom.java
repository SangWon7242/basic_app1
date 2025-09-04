package com.sbs.tutorial.app1.boudedContext.app.article.repository;

import com.sbs.tutorial.app1.boudedContext.app.article.entity.Article;

import java.util.List;

public interface ArticleRepositoryCustom {
  List<Article> getQslArticlesOrderByIdDesc();
}
