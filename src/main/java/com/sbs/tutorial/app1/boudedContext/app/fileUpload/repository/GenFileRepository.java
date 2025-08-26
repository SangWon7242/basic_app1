package com.sbs.tutorial.app1.boudedContext.app.fileUpload.repository;

import com.sbs.tutorial.app1.boudedContext.app.fileUpload.entity.GenFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenFileRepository extends JpaRepository<GenFile, Long> {
}
