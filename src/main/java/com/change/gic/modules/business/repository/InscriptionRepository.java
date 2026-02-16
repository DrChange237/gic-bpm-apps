package com.change.gic.modules.business.repository;

import com.change.gic.modules.business.entity.Inscription;
import com.change.gic.modules.business.enumeration.InscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, String> , JpaSpecificationExecutor<Inscription> {
    List<Inscription> findByStatus(InscriptionStatus insciptionStatus);

    Inscription findByReferenceAndStatus(String reference, InscriptionStatus insciptionStatus);

    Inscription findByReference(String reference);
}
