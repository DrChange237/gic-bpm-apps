package com.ccabank.feedbackservice.service.faces;

import com.ccabank.feedbackservice.entity.Answer;
import com.ccabank.feedbackservice.entity.Feedback;

import java.util.Optional;

public interface IAnswerService {

    Optional<Answer> findByFeedbackAndQuestion(Feedback feedback, String question);
}
