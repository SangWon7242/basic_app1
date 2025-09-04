package com.sbs.tutorial.app1.boudedContext.app.article.service;

import com.sbs.tutorial.app1.boudedContext.app.article.entity.Article;
import com.sbs.tutorial.app1.boudedContext.app.article.repository.ArticleRepository;
import com.sbs.tutorial.app1.boudedContext.app.fileUpload.entity.GenFile;
import com.sbs.tutorial.app1.boudedContext.app.fileUpload.service.GenFileService;
import com.sbs.tutorial.app1.boudedContext.app.member.entity.Member;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ArticleService {
  private final ArticleRepository articleRepository;
  private final GenFileService genFileService;

  public Article write(Long authorId, String title, String content) {
    return write(new Member(authorId), title, content);
  }
  
  public Article write(Member author, String title, String content) {
    Article article = Article
        .builder()
        .author(author)
        .title(title)
        .content(content)
        .build();

    articleRepository.save(article);

    return article;
  }

  public Article getArticleById(Long id) {
    return articleRepository.findById(id).orElse(null);
  }

  public void addGenFileByUrl(Article article, String typeCode, String type2Code, int fileNo, String url) {
    genFileService.addGenFileByUrl("article", article.getId(), typeCode, type2Code, fileNo, url);
  }

  public Article getForPrintArticleById(Long id) {
    Article article = getArticleById(id);

    Map<String, GenFile> genFileMap = genFileService.getRelGenFileMap(article);

    article.getExtra().put("genFileMap", genFileMap);

    return article;
  }

  public void modify(Article article, String title, String content) {
    article.setTitle(title);
    article.setContent(content);
    articleRepository.save(article);
  }

  public List<Article> getArticles() {
    return articleRepository.getQslArticlesOrderByIdDesc();
  }

  public void delete(Article article) {
    genFileService.deleteAllFilesByArticle(article);

    articleRepository.delete(article);
  }
}
