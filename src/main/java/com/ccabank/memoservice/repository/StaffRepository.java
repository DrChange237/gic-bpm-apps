package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.documenttype.sub.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
}
