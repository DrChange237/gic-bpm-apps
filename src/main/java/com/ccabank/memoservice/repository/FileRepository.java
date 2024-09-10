package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.Field;
import com.ccabank.memoservice.entity.File;
import com.ccabank.memoservice.entity.documenttype.OrdreMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    void deleteByField(Field field1);
}
