package com.ccabank.feedbackservice.repository;

import com.ccabank.feedbackservice.entity.Agency;
import com.ccabank.feedbackservice.entity.Feedback;
import com.ccabank.feedbackservice.entity.Form;
import com.ccabank.feedbackservice.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query("SELECT s FROM Staff s WHERE s.username = :username")
    Staff findByUsername(@Param("username") String username);

}
