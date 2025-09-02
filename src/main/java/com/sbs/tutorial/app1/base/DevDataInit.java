package com.sbs.tutorial.app1.base;

import com.sbs.tutorial.app1.boudedContext.app.article.entity.Article;
import com.sbs.tutorial.app1.boudedContext.app.article.service.ArticleService;
import com.sbs.tutorial.app1.boudedContext.app.member.entity.Member;
import com.sbs.tutorial.app1.boudedContext.app.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
public class DevDataInit {
  @Bean
  CommandLineRunner commandLineRunner(MemberService memberService, ArticleService articleService, PasswordEncoder passwordEncoder) {

    return args -> {
      String password = passwordEncoder.encode("1234");
      Member member1 = memberService.join("user1", password, "user1@test.com");
      memberService.setProfileImgByUrl(member1, "https://picsum.photos/200/300");

      Member member2 = memberService.join("user2", password, "user2@test.com");
      memberService.setProfileImgByUrl(member2, "https://picsum.photos/200/300");

      Article article1 = articleService.write(member1, "제목1", "내용1");
      articleService.addGenFileByUrl(article1, "common", "contentImg", 1, "https://picsum.photos/200/300");
      articleService.addGenFileByUrl(article1, "common", "contentImg", 2, "https://picsum.photos/200/300");
      articleService.addGenFileByUrl(article1, "common", "contentImg", 3, "https://picsum.photos/200/300");
      articleService.addGenFileByUrl(article1, "common", "contentImg", 4, "https://picsum.photos/200/300");

      Article article2 = articleService.write(member2, "제목2", "내용2");
      articleService.addGenFileByUrl(article1, "common", "contentImg", 1, "https://picsum.photos/200/300");
      articleService.addGenFileByUrl(article1, "common", "contentImg", 2, "https://picsum.photos/200/300");
    };
  }
}
