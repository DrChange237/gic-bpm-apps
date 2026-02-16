package com.change.gic.modules.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.change.gic.modules.core.entity.Process;
import com.change.gic.modules.core.entity.Module;

import java.util.List;

@Repository
public interface ProcessRepository extends JpaRepository<Process, String> {
    List<Process> findByModule(Module module);

}
