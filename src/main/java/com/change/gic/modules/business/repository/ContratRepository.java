package com.change.gic.modules.business.repository;

import com.change.gic.modules.business.entity.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratRepository extends JpaRepository<Contrat, String>,  JpaSpecificationExecutor<Contrat> {
    Contrat findByReference(String reference);
}
