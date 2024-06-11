package com.ccabank.feedbackservice.service.faces;

import com.ccabank.feedbackservice.domain.AppServiceResult;
import com.ccabank.feedbackservice.dto.feedback.FeedbackDto;
import com.ccabank.feedbackservice.dto.feedback.QuestionDto;

import java.util.List;

public interface IQuestionService {
    List<QuestionDto> getAllQuestions(String lang);

    List<QuestionDto> getQuestions(String form, String lang);




}
