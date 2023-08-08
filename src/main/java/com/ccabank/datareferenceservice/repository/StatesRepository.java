package com.ccabank.datareferenceservice.repository;

import com.ccabank.datareferenceservice.entity.States;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StatesRepository extends JpaRepository<States, Long> {

    @Query("SELECT s FROM States s WHERE (s.name = :name)")
    States getStateByName(@Param("name") String stateName);
}
