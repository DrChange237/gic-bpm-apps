package com.change.gic.modules.business.repository;

import com.change.gic.modules.business.entity.TestLang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestLangRepository extends JpaRepository<TestLang, String> {
    TestLang findBySlug(String testLang);

    boolean existsBySlug(String slug);
}
