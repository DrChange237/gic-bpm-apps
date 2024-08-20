package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.ProcessUnity;
import com.ccabank.memoservice.entity.documenttype.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
