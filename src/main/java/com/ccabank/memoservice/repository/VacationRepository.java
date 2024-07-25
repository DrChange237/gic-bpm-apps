package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.documenttype.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationRepository extends JpaRepository<Vacation, Long> {

}
