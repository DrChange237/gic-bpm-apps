package com.ccabank.signservice.repository;

import com.ccabank.signservice.entity.Document;
import com.ccabank.signservice.entity.SignatureRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}
