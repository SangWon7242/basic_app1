package com.sbs.tutorial.app1;

import com.sbs.tutorial.app1.boudedContext.app.home.controller.HomeController;
import com.sbs.tutorial.app1.boudedContext.app.member.controller.MemberController;
import com.sbs.tutorial.app1.boudedContext.app.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles({"base-addi", "test"})
class App1ApplicationTests {

  @Autowired
  private MockMvc mvc; // 컨트롤러 테스트 필요
  @Autowired
  private MemberService memberService;

  @Test
  @DisplayName("메인화면에서는 안녕이 나와야 한다.")
  void t01() throws Exception {
    // WHEN : 실제 동작
    // GET /
    // resultActions : 전체 결과를 담는 변수
    ResultActions resultActions = mvc.perform(get("/"))
        .andDo(print());

    // THEN : 결과 검증
    // 안녕
    // andExpect : 기대한다.
    resultActions.andExpect(status().is2xxSuccessful())
        .andExpect(handler().handlerType(HomeController.class))
        .andExpect(handler().methodName("main"))
        .andExpect(view().name("home/main"))
        .andExpect(content().string(containsString("안녕"))); // <div>안녕</div>
  }

  @Test
  @DisplayName("회원의 수")
  void t02() {
    long count = memberService.count();
    assertThat(count).isGreaterThan(0);
  }

  @Test
  @DisplayName("user1로 로그인 후 프로필 페이지에 접속하면 user1의 이메일이 보여야 한다.")
  void t03() throws Exception {
    // WHEN
    ResultActions resultActions = mvc.perform(
        get("/member/profile")
            .with(user("user1").password("1234").roles("user"))
        ).andDo(print());

    // THEN
    resultActions.andExpect(status().is2xxSuccessful())
        .andExpect(handler().handlerType(MemberController.class))
        .andExpect(handler().methodName("showProfile"))
        .andExpect(content().string(containsString("user1@test.com")));
  }

  @Test
  @DisplayName("user4로 로그인 후 프로필 페이지에 접속하면 user4의 이메일이 보여야 한다.")
  void t04() throws Exception {
// WHEN
    ResultActions resultActions = mvc.perform(
        get("/member/profile")
            .with(user("user4").password("1234").roles("user"))
    ).andDo(print());

    // THEN
    resultActions.andExpect(status().is2xxSuccessful())
        .andExpect(handler().handlerType(MemberController.class))
        .andExpect(handler().methodName("showProfile"))
        .andExpect(content().string(containsString("user4@test.com")));
  }
}
