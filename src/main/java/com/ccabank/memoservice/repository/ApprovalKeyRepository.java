package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.ApprovalKey;
import com.ccabank.memoservice.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalKeyRepository extends JpaRepository<ApprovalKey, String> {
}
