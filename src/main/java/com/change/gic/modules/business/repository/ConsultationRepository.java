package com.change.gic.modules.business.repository;

import com.change.gic.modules.business.entity.Consultation;
import com.change.gic.modules.business.entity.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, String> {
    Consultation findByInscription(Inscription inscription);
}
