package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByStaffAndStatus(String staff, RequestStatus status);
}
