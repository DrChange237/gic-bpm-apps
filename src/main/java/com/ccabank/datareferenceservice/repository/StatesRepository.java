package com.ccabank.datareferenceservice.repository;

import com.ccabank.datareferenceservice.entity.States;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatesRepository extends JpaRepository<States, Long> {

}
