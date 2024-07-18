package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.Field;
import com.ccabank.memoservice.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {
}
