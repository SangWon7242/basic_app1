package com.sbs.tutorial.app1.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
  public static String GEN_FILE_DIR_PATH;

  @Value("${custom.genFileDirPath}")
  public void setGetFileDirPath(String genFileDirPath) {
    GEN_FILE_DIR_PATH = genFileDirPath;
  }
}
