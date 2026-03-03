package com.change.gic.modules.business.repository;

import com.change.gic.modules.business.entity.Tirage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TirageRepository extends JpaRepository<Tirage, String> {
}
