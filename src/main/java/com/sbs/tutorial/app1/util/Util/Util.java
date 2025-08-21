package com.sbs.tutorial.app1.util.Util;

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
  }
}
