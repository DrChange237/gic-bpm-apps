package com.ccabank.feedbackservice.service.impl;

import com.ccabank.feedbackservice.dto.feedback.QuestionChoiceDto;
import com.ccabank.feedbackservice.dto.feedback.QuestionDto;
import com.ccabank.feedbackservice.service.faces.IQuestionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.ccabank.feedbackservice.constant.BeanIdConstant.FEEDBACK_DETAIL_SERVICE;

@Service
@Transactional
@Qualifier(FEEDBACK_DETAIL_SERVICE)
public class QuestionService implements IQuestionService {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackService.class);





    public QuestionDto getQuestion(String property){

        List<QuestionDto> questionDtos = this.getAllQuestions("fr");
        return questionDtos.stream()
                .filter(person -> person.getProperty().equals(property))
                .findFirst()
                .orElse(null);
    }


    @Override
    public List<QuestionDto> getAllQuestions(String lang) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            ClassPathResource resource = new ClassPathResource("questions/" + lang + ".json");

            InputStream inputStream = resource.getInputStream();
            List<QuestionDto> questions = objectMapper.readValue(inputStream, new TypeReference<List<QuestionDto>>() {});

            for(QuestionDto question : questions) {
                logger.error(FEEDBACK_DETAIL_SERVICE, "getQuestions : Exception ", question.getLabel());

                if(question.getType() == "bool" && question.isHaveSubQuestions()){
                    List<QuestionDto> subQuestions = new ArrayList<>();
                    if(question.getYesQuestions() != null){
                        subQuestions = this.getQuestions(question.getYesQuestions(), lang);
                        questions.addAll(subQuestions);
                    }
                    if(question.getNoQuestions() != null){
                        subQuestions = this.getQuestions(question.getNoQuestions(), lang);
                        questions.addAll(subQuestions);
                    }
                }
            }

            return questions;

        } catch (IOException e) {
            e.printStackTrace();
            logger.error(FEEDBACK_DETAIL_SERVICE, "getAllQuestions : Exception ", e.getMessage());
            return null;
        }
    }

    @Override
    public List<QuestionDto> getQuestions(String form, String lang) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClassPathResource resource = new ClassPathResource("questions/" + form + "/" + lang + ".json");
            InputStream inputStream = resource.getInputStream();
            List<QuestionDto> questions = objectMapper.readValue(inputStream, new TypeReference<List<QuestionDto>>() {});
            return questions;

        } catch (IOException e) {
            e.printStackTrace();
            logger.error(FEEDBACK_DETAIL_SERVICE, "getQuestions : Exception ", e.getMessage());
            return null;
        }
    }
}
