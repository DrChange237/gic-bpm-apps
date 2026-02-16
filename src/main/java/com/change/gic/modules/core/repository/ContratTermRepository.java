package com.change.gic.modules.core.repository;

import com.change.gic.modules.business.entity.ContratTerm;
import com.change.gic.modules.business.entity.ContratTermGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratTermRepository  extends JpaRepository<ContratTerm, String> {
    boolean existsByCode(String code);

    List<ContratTerm> findByGroup(ContratTermGroup prepDoc);

    ContratTerm findByCode(String term);

    List<ContratTerm> findAllByOrderByPositionAsc();

    List<ContratTerm> findByGroupOrderByPositionAsc(ContratTermGroup groupDebours);
}
