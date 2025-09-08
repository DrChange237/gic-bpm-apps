package com.ccabank.paperless.repository;

import com.ccabank.paperless.entity.File;
import com.ccabank.paperless.entity.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface FileRepository extends JpaRepository<File, Long> , JpaSpecificationExecutor<File> {
    List<File> findByRequest(Request request);

    List<File> findByCreationDateBetween(LocalDate startDate, LocalDate endDate);


    Page<File> findAllByOrderByAddDateDesc(Specification<File> spec, Pageable pageable);
}
