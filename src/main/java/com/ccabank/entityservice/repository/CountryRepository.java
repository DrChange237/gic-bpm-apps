package com.ccabank.entityservice.repository;

import com.ccabank.entityservice.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    @Query("SELECT c FROM Country c WHERE (c.countryName = :name)")
    Country getCountryByName(@Param("name") String countryName);

    @Query("SELECT c FROM Country c WHERE (c.code = :code)")
    Country getCountryByCode(@Param("code") String code);

    @Query("SELECT c FROM Country c WHERE (c.codeIso3 = :code)")
    Country getCountryByCodeIso3(@Param("code") String codeIso3);

    @Query("SELECT c FROM Country c WHERE (c.phoneCode = :code)")
    Country getCountryByPhoneCode(@Param("code") String phoneCode);

    @Query("SELECT c FROM Country c WHERE c.isActive = true")
    List<Country> getActiveCountry();

    @Query("SELECT c FROM Country c WHERE c.isActive = false")
    List<Country> getNonActiveCountry();


}
