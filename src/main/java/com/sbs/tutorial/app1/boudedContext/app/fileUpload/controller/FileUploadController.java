package com.sbs.tutorial.app1.boudedContext.app.fileUpload.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/upload")
public class FileUploadController {
  @Value("${custom.genFileDirPath}") // 설정 파일을 가져온다.
  private String genFileDirPath;

  @RequestMapping("")
  @ResponseBody
  public String upload(@RequestParam("img1") MultipartFile img1, @RequestParam("img2") MultipartFile img2) {

    File dir = new File(genFileDirPath);

    if (!dir.exists()) { // 디렉토리 존재 여부 확인
      dir.mkdirs(); // 디렉토리생성
    }

    try {
      img1.transferTo(new File(dir, "1.png"));
      img2.transferTo(new File(dir, "2.png"));
      return "파일 업로드 완료";
    } catch (IOException e) {
      e.printStackTrace();
      return "파일 업로드 실행 : " + e.getMessage();
    }
  }
}
