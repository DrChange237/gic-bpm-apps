package com.ccabank.feedbackservice.repository;

import com.ccabank.feedbackservice.entity.Agency;
import com.ccabank.feedbackservice.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {

    @Query("SELECT a FROM Agency a WHERE a.agencyCode =:agencyCode")
    Agency findAgencyByAgencyCode(@Param("agencyCode") String agencyCode);

}
