package com.ccabank.paperless.repository;

import com.ccabank.paperless.entity.camunda.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface GroupRepository extends JpaRepository<Group, String> {

}
