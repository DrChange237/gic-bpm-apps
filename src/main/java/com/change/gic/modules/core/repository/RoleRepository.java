package com.change.gic.modules.core.repository;

import com.change.gic.modules.core.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findBySlug(String role);
}
