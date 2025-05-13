package com.ccabank.paperless.repository;

import com.ccabank.paperless.entity.ApprovalKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalKeyRepository extends JpaRepository<ApprovalKey, String> {
}
