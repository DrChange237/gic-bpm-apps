package com.ccabank.paperless.repository;

import com.ccabank.paperless.entity.camunda.MemberShip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberShipRepository extends JpaRepository<MemberShip, String> {

}
