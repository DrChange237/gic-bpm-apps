package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.documenttype.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationRepository extends JpaRepository<Vacation, Long> {

}
