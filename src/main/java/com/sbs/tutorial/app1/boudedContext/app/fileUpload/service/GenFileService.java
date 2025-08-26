package com.sbs.tutorial.app1.boudedContext.app.fileUpload.service;

import com.sbs.tutorial.app1.boudedContext.app.article.entity.Article;
import com.sbs.tutorial.app1.boudedContext.app.fileUpload.entity.GenFile;
import com.sbs.tutorial.app1.boudedContext.app.fileUpload.repository.GenFileRepository;
import com.sbs.tutorial.app1.util.Util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenFileService {
  private final GenFileRepository genFileRepository;

  public void saveFiles(Article article, Map<String, MultipartFile> fileMap) {
    String relTypeCode = "article";
    long relId = article.getId();

    for (String inputName : fileMap.keySet()) {
      MultipartFile multipartFile = fileMap.get(inputName);

      String[] inputNameBits = inputName.split("__"); // common__contentImg__1" : ["common", "contentImg", "1"]


      String typeCode = inputNameBits[0]; // 파일 타입
      String type2Code = inputNameBits[1]; // 세부 타입(write.html에 있는 name)
      String originFileName = multipartFile.getOriginalFilename(); // 원본 파일명
      String fileExt = Util.file.getExt(originFileName); // 파일 확장자
      String fileExtTypeCode = Util.file.getFileExtTypeCodeFromFileExt(fileExt); // 확장 타입
      String fileExtType2Code = Util.file.getFileExtType2CodeFromFileExt(fileExt);; // 확장 타입
      int fileNo = Integer.parseInt(inputNameBits[2]); // 파일 번호
      int fileSize = (int)multipartFile.getSize(); // 파일 크기
      String fileDir = relTypeCode + "/" + Util.date.getCurrentDateFormatted("yyyy_MM_dd");

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
    }
  }
}
