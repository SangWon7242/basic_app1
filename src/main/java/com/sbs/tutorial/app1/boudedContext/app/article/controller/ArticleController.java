package com.sbs.tutorial.app1.boudedContext.app.article.controller;

import com.sbs.tutorial.app1.base.dto.RsData;
import com.sbs.tutorial.app1.boudedContext.app.article.entity.Article;
import com.sbs.tutorial.app1.boudedContext.app.article.input.ArticleForm;
import com.sbs.tutorial.app1.boudedContext.app.article.service.ArticleService;
import com.sbs.tutorial.app1.boudedContext.app.fileUpload.entity.GenFile;
import com.sbs.tutorial.app1.boudedContext.app.fileUpload.service.GenFileService;
import com.sbs.tutorial.app1.boudedContext.app.security.dto.MemberContext;
import com.sbs.tutorial.app1.util.Util.Util;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
@Slf4j
public class ArticleController {
  private final ArticleService articleService;
  private final GenFileService genFileService;

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/write")
  public String shoWrite() {
    return "article/write";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/write")
  public String write(@AuthenticationPrincipal MemberContext memberContext,
                      @Valid ArticleForm articleForm,
                      MultipartRequest multipartRequest,
                      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "article/write";
    }

    Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

    Article article = articleService.write(memberContext.getId(), articleForm.getTitle(), articleForm.getContent());

    RsData<Map<String, GenFile>> saveFilesFileRsData = genFileService.saveFiles(article, fileMap);

    log.debug("saveFilesFileRsData : {}", saveFilesFileRsData);

    String msg = "%d번 게시물이 작성되었습니다.".formatted(article.getId());
    msg = Util.url.encode(msg); // 인코딩 처리;

    return "redirect:/article/%d?msg=%s".formatted(article.getId(), msg);
  }

  @GetMapping("/{id}")
  public String showDetail(Model model, @PathVariable Long id) {
    Article article = articleService.getForPrintArticleById(id);

    if (article == null) {
      return "redirect:/";
    }

    model.addAttribute("article", article);

    return "article/detail";
  }

  @GetMapping("/list")
  public String showList(Model model) {
    List<Article> articles = articleService.getArticles();

    model.addAttribute("articles", articles);

    return "article/list";
  }

  @GetMapping("/{id}/json/forDebug")
  @ResponseBody
  public Article showDetail(@PathVariable Long id) {
    return articleService.getForPrintArticleById(id);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/{id}/modify")
  public String showModify(@AuthenticationPrincipal MemberContext memberContext, Model model, @PathVariable Long id) {
    Article article = articleService.getForPrintArticleById(id);

    // 권한에 대한 접근이 없는 경우
    if (memberContext.getId() != article.getAuthor().getId()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
    }

    model.addAttribute("article", article);

    return "article/modify";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/{id}/modify")
  public String modify(
      @AuthenticationPrincipal MemberContext memberContext,
      Model model,
      @PathVariable Long id,
      @Valid ArticleForm articleForm,
      BindingResult bindingResult,
      MultipartRequest multipartRequest,
      @RequestParam Map<String, String> params) {

    Article article = articleService.getForPrintArticleById(id);

    // 권한에 대한 접근이 없는 경우
    if (memberContext.getId() != article.getAuthor().getId()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
    }

    if(bindingResult.hasErrors()) {
      model.addAttribute("article", article);
      return "article/modify"; // 에러가 발생하면 수정 폼으로 돌아감
    }

    Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

    genFileService.deleteFiles(article, params);
    RsData<Map<String, GenFile>> saveFilesFileRsData = genFileService.saveFiles(article, fileMap);

    articleService.modify(article, articleForm.getTitle(), articleForm.getContent());

    String msg = "%d번 게시물이 수정되었습니다.".formatted(article.getId());
    msg = Util.url.encode(msg);

    return "redirect:/article/%d?msg=%s".formatted(article.getId(), msg);
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/{id}/delete")
  public String delete(@AuthenticationPrincipal MemberContext memberContext,
                           @PathVariable Long id) {
    Article article = articleService.getForPrintArticleById(id);

    // 권한에 대한 접근이 없는 경우
    if (memberContext.getId() != article.getAuthor().getId()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
    }
    
    articleService.delete(article);

    String msg = "%d번 게시물이 삭제되었습니다.".formatted(article.getId());
    msg = Util.url.encode(msg);
    
    return "redirect:/article/list?msg=%s".formatted(msg);
  }
}
