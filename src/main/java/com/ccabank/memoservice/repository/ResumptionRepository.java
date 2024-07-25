package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.documenttype.Resumption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumptionRepository extends JpaRepository<Resumption, Long> {

}
