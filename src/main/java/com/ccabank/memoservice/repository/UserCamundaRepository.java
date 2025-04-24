package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.camunda.UserCamunda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface UserCamundaRepository extends JpaRepository<UserCamunda, String> {

    Optional<UserCamunda> findByUserName(String userName);
}
