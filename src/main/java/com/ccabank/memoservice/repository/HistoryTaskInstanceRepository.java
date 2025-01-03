package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.camunda.HistoryTaskInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryTaskInstanceRepository extends JpaRepository<HistoryTaskInstance, String> {

}
