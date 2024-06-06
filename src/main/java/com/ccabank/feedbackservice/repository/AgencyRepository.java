package com.ccabank.feedbackservice.repository;

import com.ccabank.feedbackservice.entity.Agency;
import com.ccabank.feedbackservice.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AgencyRepository extends JpaRepository<Feedback, Long> {

    @Query("SELECT f FROM Agency f WHERE f.agencyCode = :agencyCode")
    Agency findAgencyByAgencyCode(String agencyCode);

}
