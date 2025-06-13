package com.ccabank.paperless.repository;

import com.ccabank.paperless.entity.File;
import com.ccabank.paperless.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByRequest(Request request);
}
