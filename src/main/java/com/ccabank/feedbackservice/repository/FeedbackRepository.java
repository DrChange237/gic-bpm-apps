package com.ccabank.feedbackservice.repository;

import com.ccabank.feedbackservice.entity.Agency;
import com.ccabank.feedbackservice.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("SELECT f FROM Feedback f WHERE f.staffUsername = :staff AND  f.createdAt BETWEEN :startDate AND :endDate")
    List<Feedback> findFeedbackByStaffUsernameAndCreatedAtBetween(@Param("staff") String staff, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT f FROM Feedback f WHERE f.agency = :agency AND  f.createdAt BETWEEN :startDate AND :endDate")
    List<Feedback> findFeedbackByAgencyAndCreatedAtBetween(@Param("agency") Agency agency, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    @Query("SELECT f FROM Feedback f WHERE f.staffUsername = :staff")
    List<Feedback> findFeedbackByStaff(String staff);


    @Query("SELECT f FROM Feedback f WHERE  f.createdAt BETWEEN :startDate AND :endDate ORDER BY :property DESC ")
    Page<Feedback> findRank(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate, String property);



}
