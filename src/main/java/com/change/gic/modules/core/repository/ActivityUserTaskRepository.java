package com.change.gic.modules.core.repository;

import com.change.gic.modules.core.entity.ActivityUserTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityUserTaskRepository extends JpaRepository<ActivityUserTask, String> {

    ActivityUserTask findByTaskDefinitionKey(String taskDefinitionKey);

    boolean existsByTaskDefinitionKey(String taskDefinitionKey);
}
