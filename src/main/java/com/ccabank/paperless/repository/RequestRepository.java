package com.ccabank.paperless.repository;

import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByStaffAndStatus(String staff, RequestStatus status);

    @Query("SELECT COUNT(r) FROM Request r WHERE DATE(r.createdAt) = CURRENT_DATE")
    long countRequestsCreatedToday();

    Request findOneByReference(String reference);

    List<Request> findByOrderByLastModificationDesc();


    List<Request> findByStaffAndArchivedOrderByLastModificationDesc(String staff, boolean archived);

    Request findByInstanceId(String instanceId);

    List<Request> findByStatus(RequestStatus requestStatus);
}
