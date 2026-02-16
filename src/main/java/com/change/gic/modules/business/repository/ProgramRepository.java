package com.change.gic.modules.business.repository;

import com.change.gic.modules.business.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, String> {
    Program findByName(String program);

    boolean existsByName(String name);
}
