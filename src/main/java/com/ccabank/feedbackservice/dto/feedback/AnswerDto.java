package com.ccabank.feedbackservice.dto.feedback;

import com.ccabank.feedbackservice.entity.Feedback;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.ccabank.feedbackservice.constant.BeanIdConstant.FEEDBACK_DETAIL_SERVICE;

public class AnswerDto {

    private int id;

    @NotNull(message = "question cannot be null")
    private String question;

    private String label;

    private QuestionDto questionDto;

    private String answer = "";

    @NotNull(message = "feedback cannot be null")
    @JsonIgnore
    private FeedbackDto feedback;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getLabel() {
        String label = this.getQuestion(this.question).getLabel();
        if(label == null){
            return "";
        }
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public FeedbackDto getFeedback() {
        return feedback;
    }

    public void setFeedback(FeedbackDto feedback) {
        this.feedback = feedback;
    }

    public QuestionDto getQuestionDto(){

        List<QuestionDto> questionDtos = this.getAllQuestions("fr");
        return questionDtos.stream()
                .filter(question -> question.getProperty().equals(this.question))
                .findFirst()
                .orElse(null);
    }

    public void setQuestionDto(QuestionDto questionDto) {
        this.questionDto = questionDto;
    }

    public QuestionDto getQuestion(String property){

        List<QuestionDto> questionDtos = this.getAllQuestions("fr");
        return questionDtos.stream()
                .filter(person -> person.getProperty().equals(property))
                .findFirst()
                .orElse(null);
    }

    public List<QuestionDto> getAllQuestions(String lang) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            ClassPathResource resource = new ClassPathResource("questions/" + lang + ".json");

            InputStream inputStream = resource.getInputStream();
            List<QuestionDto> questions = objectMapper.readValue(inputStream, new TypeReference<List<QuestionDto>>() {});

            return questions;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
