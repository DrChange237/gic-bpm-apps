package com.change.gic.modules.business.repository;

import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.TestExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestExamRepository extends JpaRepository<TestExam, String> {
    Optional<TestExam> findByContract(Contrat contract);
}
