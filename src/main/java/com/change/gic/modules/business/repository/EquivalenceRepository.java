package com.change.gic.modules.business.repository;

import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.Equivalence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquivalenceRepository extends JpaRepository<Equivalence, String> {
    Optional<Equivalence> findByContract(Contrat contrat);
}
