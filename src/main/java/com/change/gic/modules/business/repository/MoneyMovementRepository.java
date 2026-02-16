package com.change.gic.modules.business.repository;

import com.change.gic.modules.business.entity.MoneyMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyMovementRepository extends JpaRepository<MoneyMovement, String> {
}
