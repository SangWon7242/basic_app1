package com.sbs.tutorial.app1.util.Util;

import org.apache.tika.Tika;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Util {
  // 중첩 클래스
  public static class date {

    public static String getCurrentDateFormatted(String pattern) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
      String formattedDate = LocalDateTime.now().format(formatter);

      return formattedDate;
    }
  }
  
  // 파일의 확장자 명을 가져오는 코드
  public static class file {
    public static String getExt(String fileName) {
      return Optional.ofNullable(fileName)
          .filter(f -> f.contains("."))
          .map(f -> f.substring(f.lastIndexOf(".") + 1))
          .orElse("");
    }

    public static String downloadImg(String url, String filePath) {
      new File(filePath).getParentFile().mkdirs();

      byte[] imageBytes = new RestTemplate().getForObject(url, byte[].class);
      try {
        Files.write(Paths.get(filePath), imageBytes);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      String mimeType = null;
      try {
        mimeType = new Tika().detect(new File(filePath));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      String ext = mimeType.replaceAll("image/", "");
      ext = ext.replaceAll("jpeg", "jpg");

      String newFilePath = filePath + "." + ext;

      new File(filePath).renameTo(new File(newFilePath));

      // c:/spring-temp/app1/member/2025_08_21/asdasdas
      // tika를 이용해서 확장자 이름을 알아냄
      // c:/spring-temp/app1/member/2025_08_21/asdasdas.jpg

      return newFilePath;
    }

    public static String getFileExtTypeCodeFromFileExt(String ext) {
      switch (ext) {
        case "jpeg":
        case "jpg":
        case "gif":
        case "png":
          return "img";
        case "mp4":
        case "avi":
        case "mov":
          return "video";
        case "mp3":
          return "audio";
      }

      return "etc";
    }

    public static String getFileExtType2CodeFromFileExt(String ext) {

      switch (ext) {
        case "jpeg":
        case "jpg":
          return "jpg";
        case "gif":
          return ext;
        case "png":
          return ext;
        case "mp4":
          return ext;
        case "mov":
          return ext;
        case "avi":
          return ext;
        case "mp3":
          return ext;
      }

      return "etc";
    }
  }
}
