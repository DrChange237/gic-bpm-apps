package com.change.gic.modules.business.repository;

import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.SelectionExpress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SelectionExpressRepository extends JpaRepository<SelectionExpress, String> {
    Optional<SelectionExpress> findByContract(Contrat contract);
}
