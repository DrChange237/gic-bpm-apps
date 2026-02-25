package com.change.gic.modules.business.repository;

import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.PermanentResident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermanentResidentRepository extends JpaRepository<PermanentResident, String> {
    Optional<PermanentResident> findByContract(Contrat contrat);
}
