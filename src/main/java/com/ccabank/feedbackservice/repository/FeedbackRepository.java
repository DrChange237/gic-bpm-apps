package com.ccabank.feedbackservice.repository;

import com.ccabank.feedbackservice.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("SELECT f FROM Feedback f WHERE f.staffUsername = :staff AND  f.createdAt BETWEEN :startDate AND :endDate")
    List<Feedback> findFeedbackByStaffUsernameAndCreatedAtBetween(String staff, LocalDateTime startDate, LocalDateTime endDate);

}
