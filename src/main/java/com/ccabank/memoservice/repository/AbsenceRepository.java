package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.documenttype.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {

}
