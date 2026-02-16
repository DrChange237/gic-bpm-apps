package com.change.gic.modules.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.change.gic.modules.core.entity.Module;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, String> {
    List<Module> findAllByOrderByPositionAsc();
}
