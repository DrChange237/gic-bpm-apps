package com.ccabank.paperless.repository;

import com.ccabank.paperless.entity.camunda.UserCamunda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;




public interface UserCamundaRepository extends JpaRepository<UserCamunda, String> {
    Optional<UserCamunda> findByUserName(String userName);
}
