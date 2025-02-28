package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.camunda.MemberShip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberShipRepository extends JpaRepository<MemberShip, String> {

}
