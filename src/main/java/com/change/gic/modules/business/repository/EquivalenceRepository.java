package com.change.gic.modules.business.repository;

import com.change.gic.modules.business.entity.Equivalence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquivalenceRepository extends JpaRepository<Equivalence, String> {
}
