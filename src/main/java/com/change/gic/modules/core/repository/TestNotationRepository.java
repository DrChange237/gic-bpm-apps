package com.change.gic.modules.core.repository;

import com.change.gic.modules.business.entity.TestLang;
import com.change.gic.modules.business.entity.TestNotation;

import com.change.gic.modules.business.enumeration.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestNotationRepository extends JpaRepository<TestNotation, String> {
    List<TestNotation> findByExamAndMatiereOrderByLevelDesc(TestLang testLang, Matiere matiere);
}
