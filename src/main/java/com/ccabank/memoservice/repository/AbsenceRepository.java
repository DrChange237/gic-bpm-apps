package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.documenttype.Absence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {

}
