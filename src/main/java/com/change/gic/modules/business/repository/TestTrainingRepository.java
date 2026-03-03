package com.change.gic.modules.business.repository;

import com.change.gic.modules.business.entity.TestTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestTrainingRepository extends JpaRepository<TestTraining, String> {

}
