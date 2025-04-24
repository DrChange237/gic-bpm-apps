package com.ccabank.paperless.repository;

import com.ccabank.paperless.entity.Approbation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApprobationRepository extends JpaRepository<Approbation, Long> {
    Optional<Approbation> findByTaskId(String id);
}
