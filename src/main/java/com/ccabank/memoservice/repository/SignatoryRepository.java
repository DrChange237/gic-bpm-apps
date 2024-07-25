package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignatoryRepository extends JpaRepository<Signatory, Long> {
}
