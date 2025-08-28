package com.sbs.tutorial.app1.boudedContext.app.fileUpload.service;

import com.sbs.tutorial.app1.base.AppConfig;
import com.sbs.tutorial.app1.base.dto.RsData;
import com.sbs.tutorial.app1.boudedContext.app.article.entity.Article;
import com.sbs.tutorial.app1.boudedContext.app.fileUpload.entity.GenFile;
import com.sbs.tutorial.app1.boudedContext.app.fileUpload.repository.GenFileRepository;
import com.sbs.tutorial.app1.util.Util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenFileService {
  private final GenFileRepository genFileRepository;

  private String getCurrentDirName(String relTypeCode) {
    return relTypeCode + "/" + Util.date.getCurrentDateFormatted("yyyy_MM_dd");
  }

  public RsData<Map<String, GenFile>> saveFiles(Article article, Map<String, MultipartFile> fileMap) {
    String relTypeCode = "article";
    long relId = article.getId();

    Map<String, GenFile> genfileIds = new HashMap<>();

    for (String inputName : fileMap.keySet()) {
      MultipartFile multipartFile = fileMap.get(inputName);

      if(multipartFile.isEmpty()) continue;

      String[] inputNameBits = inputName.split("__"); // common__contentImg__1" : ["common", "contentImg", "1"]

      String typeCode = inputNameBits[0]; // 파일 타입
      String type2Code = inputNameBits[1]; // 세부 타입(write.html에 있는 name)
      String originFileName = multipartFile.getOriginalFilename(); // 원본 파일명
      String fileExt = Util.file.getExt(originFileName); // 파일 확장자
      String fileExtTypeCode = Util.file.getFileExtTypeCodeFromFileExt(fileExt); // 확장 타입
      String fileExtType2Code = Util.file.getFileExtType2CodeFromFileExt(fileExt);; // 확장 타입
      int fileNo = Integer.parseInt(inputNameBits[2]); // 파일 번호
      int fileSize = (int)multipartFile.getSize(); // 파일 크기
      String fileDir = getCurrentDirName(relTypeCode);

      GenFile genFile = GenFile.builder()
          .relTypeCode(relTypeCode)
          .relId(relId)
          .typeCode(typeCode)
          .type2Code(type2Code)
          .fileExtTypeCode(fileExtTypeCode)
          .fileExtType2Code(fileExtType2Code)
          .fileSize(fileSize)
          .fileNo(fileNo)
          .fileExt(fileExt)
          .fileDir(fileDir)
          .originFileName(originFileName)
          .build();

      genFileRepository.save(genFile);

      String filePath = AppConfig.GEN_FILE_DIR_PATH + "/" + fileDir + "/" + genFile.getFileName();

      File file = new File(filePath);
      file.getParentFile().mkdirs();

      try {
        multipartFile.transferTo(file); // 파일 저장
      } catch (Exception e) {
        e.printStackTrace();
      }

      genfileIds.put(inputName, genFile);
    }

    return new RsData<>("S-1", "파일을 업로드했습니다.", genfileIds);
  }

  public void addGenFileByUrl(String relTypeCode, Long relId, String typeCode, String type2Code, int fileNo, String url) {
    String fileDir = getCurrentDirName(relTypeCode);

    String downFilePath = Util.file.downloadImg(url, AppConfig.GEN_FILE_DIR_PATH + "/" + fileDir + "/" + UUID.randomUUID());

    File dowonloadedFile = new File(downFilePath);

    String originFileName = dowonloadedFile.getName();
    String fileExt = Util.file.getExt(originFileName); // 파일 확장자
    String fileExtTypeCode = Util.file.getFileExtTypeCodeFromFileExt(fileExt); // 확장 타입
    String fileExtType2Code = Util.file.getFileExtType2CodeFromFileExt(fileExt); // 확장 타입
    int fileSize = 0;

    try {
      fileSize = (int) Files.size(Paths.get(downFilePath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    GenFile genFile = GenFile.builder()
        .relTypeCode(relTypeCode)
        .relId(relId)
        .typeCode(typeCode)
        .type2Code(type2Code)
        .fileExtTypeCode(fileExtTypeCode)
        .fileExtType2Code(fileExtType2Code)
        .fileSize(fileSize)
        .fileNo(fileNo)
        .fileExt(fileExt)
        .fileDir(fileDir)
        .originFileName(originFileName)
        .build();

    genFileRepository.save(genFile);

    String filePath = AppConfig.GEN_FILE_DIR_PATH + "/" + fileDir + "/" + genFile.getFileName();

    File file = new File(filePath);

    file.getParentFile().mkdirs();

    dowonloadedFile.renameTo(file);
  }

  public Map<String, GenFile> getRelGenFileMap(Article article) {
    List<GenFile> genFiles = genFileRepository.findByRelTypeCodeAndRelIdOrderByTypeCodeAscType2CodeAscFileNoAsc("article", article.getId());

    // genFile 의 정렬상태를 LinkedHashMap 를 이용하여 순서를 보장하도록
    return genFiles
        .stream()
        .collect(Collectors.toMap(
            genFile -> genFile.getTypeCode() + "__" + genFile.getType2Code() + "__" + genFile.getFileNo(),
            genFile -> genFile,
            (genFile1, genFile2) -> genFile1,
            LinkedHashMap::new
        ));
  }
}
