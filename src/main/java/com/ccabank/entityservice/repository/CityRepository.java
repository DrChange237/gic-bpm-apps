package com.ccabank.entityservice.repository;

import com.ccabank.entityservice.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Query("SELECT c FROM City c WHERE (c.name = :name)")
    City getCityByName(@Param("name") String name);

    @Query("SELECT c FROM City c WHERE (c.states.id = :id)")
    List<City> getCityByState(@Param("id") Long stateId);
}
