package com.ccabank.feedbackservice.repository;

import com.ccabank.feedbackservice.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;


@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findOneByFeedbackAndQuestion(@Param("feedback") Feedback feedback, @Nullable @Param("question") String question);

    @Query("SELECT DISTINCT a.answer FROM Answer a WHERE a.question =:question")
    List<String> findDistinctByQuestion(@Param("question") String question);

}
