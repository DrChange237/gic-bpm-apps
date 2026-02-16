package com.change.gic.modules.business.repository;

import com.change.gic.modules.business.entity.TestExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestExamRepository extends JpaRepository<TestExam, String> {
}
