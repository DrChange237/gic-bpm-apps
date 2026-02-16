package com.change.gic.modules.core.repository;

import com.change.gic.modules.business.entity.ContratTermGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratTermGroupRepository extends JpaRepository<ContratTermGroup, String> {
    boolean existsByCode(String code);

    ContratTermGroup findByCode(String prepDoc);

    List<ContratTermGroup> findAllByOrderByPositionAsc();
}
