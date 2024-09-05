package com.ccabank.memoservice.repository;


import com.ccabank.memoservice.entity.Approval;
import com.ccabank.memoservice.entity.ApprovalStatus;
import com.ccabank.memoservice.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {

    List<Approval> findByStaffAndStatus(String staff, ApprovalStatus approvalStatus);

    List<Approval> findByRequestAndStatus(Request request, ApprovalStatus approvalStatus);

    @Query("SELECT a FROM Approval a JOIN a.processUnity p WHERE p.staffList LIKE CONCAT('%', :username, '%') AND a.status = :status")
    List<Approval> findApprovalsByUsernameInStaffListAndStatus(@Param("username") String username, @Param("status") ApprovalStatus status);
}
