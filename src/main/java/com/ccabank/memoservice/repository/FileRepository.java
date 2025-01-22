package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.File;
import com.ccabank.memoservice.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByRequest(Request request);
}
