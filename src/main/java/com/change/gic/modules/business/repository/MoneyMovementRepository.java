package com.change.gic.modules.business.repository;

import com.change.gic.modules.business.entity.MoneyMovement;
import com.change.gic.modules.business.info.MoneyMovementInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoneyMovementRepository extends JpaRepository<MoneyMovement, String> {
    List<MoneyMovement> findByReference(String reference);
}
