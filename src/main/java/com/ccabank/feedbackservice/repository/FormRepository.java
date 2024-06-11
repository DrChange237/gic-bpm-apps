package com.ccabank.feedbackservice.repository;

import com.ccabank.feedbackservice.entity.Agency;
import com.ccabank.feedbackservice.entity.Feedback;
import com.ccabank.feedbackservice.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FormRepository extends JpaRepository<Form, Long> {

}
