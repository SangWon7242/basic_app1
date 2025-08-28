package com.sbs.tutorial.app1.boudedContext.app.fileUpload.repository;

import com.sbs.tutorial.app1.boudedContext.app.fileUpload.entity.GenFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenFileRepository extends JpaRepository<GenFile, Long> {
  List<GenFile> findByRelTypeCodeAndRelIdOrderByTypeCodeAscType2CodeAscFileNoAsc(String article, Long id);
}
