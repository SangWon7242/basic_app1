package com.sbs.tutorial.app1.boudedContext.app.article.controller;

import com.sbs.tutorial.app1.boudedContext.app.article.entity.Article;
import com.sbs.tutorial.app1.boudedContext.app.article.input.ArticleForm;
import com.sbs.tutorial.app1.boudedContext.app.article.service.ArticleService;
import com.sbs.tutorial.app1.boudedContext.app.fileUpload.service.GenFileService;
import com.sbs.tutorial.app1.boudedContext.app.security.dto.MemberContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

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
  @ResponseBody
  public String write(@AuthenticationPrincipal MemberContext memberContext,
                      @Valid ArticleForm articleForm,
                      MultipartRequest multipartRequest,
                      BindingResult bindingResult) {
    if(bindingResult.hasErrors()) {
      return "article/write";
    }

    Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

    log.debug("fileMap : {}", fileMap);

    Article article = articleService.write(memberContext.getId(), articleForm.getTitle(), articleForm.getContent());

    genFileService.saveFiles(article, fileMap);

    return "작성중";
  }
}
