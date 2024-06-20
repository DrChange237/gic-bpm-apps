package com.ccabank.feedbackservice.service.impl;


import com.ccabank.feedbackservice.entity.Answer;
import com.ccabank.feedbackservice.entity.Feedback;
import com.ccabank.feedbackservice.repository.AnswerRepository;
import com.ccabank.feedbackservice.service.faces.IAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ccabank.feedbackservice.constant.BeanIdConstant.FEEDBACK_DETAIL_SERVICE;

@Service
@Transactional
@Qualifier(FEEDBACK_DETAIL_SERVICE)
public class AnswerService implements IAnswerService {


    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public Optional<Answer> findByFeedbackAndQuestion(Feedback feedback, String question){
        Optional<Answer> answer = answerRepository.findOneByFeedbackAndQuestion(feedback, question);
        return answer;
    }

    @Override
    public List<String> findDistinctByQuestion(String question){
        List<String> answers = answerRepository.findDistinctByQuestion(question);
        return answers;
    }
}
