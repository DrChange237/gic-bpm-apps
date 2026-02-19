package com.change.gic.modules.business.repository;


import com.change.gic.modules.business.entity.OrgEquivalence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgEquivalenceRepository extends JpaRepository<OrgEquivalence, String> {
}
