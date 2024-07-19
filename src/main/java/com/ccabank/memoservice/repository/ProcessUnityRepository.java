package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.Field;
import com.ccabank.memoservice.entity.ProcessUnity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessUnityRepository extends JpaRepository<ProcessUnity, Long> {
    ProcessUnity findOneByCode(String code);
}
