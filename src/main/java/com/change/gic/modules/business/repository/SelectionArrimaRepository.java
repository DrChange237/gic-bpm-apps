package com.change.gic.modules.business.repository;

import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.SelectionArrima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SelectionArrimaRepository extends JpaRepository<SelectionArrima, String> {
    Optional<SelectionArrima> findByContract(Contrat contrat);
}
