package com.change.gic.modules.file.repository;

import com.change.gic.modules.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, String> {
    List<File> findAllByProject(String project);

    List<File> findAllByProjectAndPathContainingIgnoreCase(String project, String path);

    File findByUrl(String fileId);
}
