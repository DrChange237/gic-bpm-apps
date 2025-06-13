package com.ccabank.paperless.repository;

import com.ccabank.paperless.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {

    boolean existsByStructure(String structure);
    DocumentType findOneByStructure(String documentType);

    List<DocumentType> findByVisible(boolean b);
}
