package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.Approbation;
import com.ccabank.memoservice.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Repository
public interface ApprobationRepository extends JpaRepository<Approbation, Long> {
    Optional<Approbation> findByTaskId(String id);
}
