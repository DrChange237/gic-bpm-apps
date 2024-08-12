package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.documenttype.Resumption;
import com.ccabank.memoservice.entity.documenttype.sub.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {
}
