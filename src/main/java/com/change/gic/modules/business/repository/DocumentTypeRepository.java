package com.change.gic.modules.business.repository;

import com.change.gic.modules.business.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, String>{

    boolean existsByTag(String tag);

    DocumentType findByTag(String documentType);
}
