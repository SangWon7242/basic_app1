package com.sbs.tutorial.app1.boudedContext.app.fileUpload.controller;

import lombok.RequiredArgsConstructor;
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
  @RequestMapping("")
  @ResponseBody
  public String upload(@RequestParam("img1") MultipartFile file) {
    // 저장 경로 설정
    String uploadDir = "c:/spring-temp/app1";
    String fileName = "1.png";

    // 디렉토리 생성
    File dir = new File(uploadDir);

    if (!dir.exists()) { // 디렉토리 존재 여부 확인
      dir.mkdirs(); // 디렉토리생성
    }

    // 파일 저장
    File testFile = new File(dir, fileName);

    try {
      file.transferTo(testFile); // 파일 저장
      return "파일 업로드 완료 : " + testFile.getAbsolutePath(); // getAbsolutePath : 파일 전체 경로
    } catch (IOException e) {
      e.printStackTrace();
      return "파일 업로드 실행 : " + e.getMessage();
    }
  }
}
