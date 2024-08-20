package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.documenttype.Vacation;
import com.ccabank.memoservice.entity.documenttype.WorkForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkFormRepository extends JpaRepository<WorkForm, Long>  {
}
