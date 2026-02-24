package com.change.gic.modules.core.repository;

import com.change.gic.modules.core.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, String> {
    List<Document> findByBusinessKey(String businessKey);

    Document findByBusinessKeyAndTag(String reference, String tag);
}
