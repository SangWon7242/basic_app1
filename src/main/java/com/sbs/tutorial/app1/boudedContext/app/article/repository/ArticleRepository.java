package com.sbs.tutorial.app1.boudedContext.app.article.repository;

import com.sbs.tutorial.app1.boudedContext.app.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
