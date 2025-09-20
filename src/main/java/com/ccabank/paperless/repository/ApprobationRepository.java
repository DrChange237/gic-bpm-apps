package com.ccabank.paperless.repository;

import com.ccabank.paperless.entity.Approbation;
import com.ccabank.paperless.entity.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApprobationRepository extends JpaRepository<Approbation, Long> {
    Optional<Approbation> findByTaskId(String id);

    List<Approbation> findByStaffAndStatus(String username, ApprovalStatus approvalStatus);

    List<Approbation> findByReferenceAndStaffAndStatus(String reference, String userTaskId, ApprovalStatus approvalStatus);

    List<Approbation> findByReferenceAndStatusOrderByCreationDateDesc(String reference, ApprovalStatus approvalStatus);
}
