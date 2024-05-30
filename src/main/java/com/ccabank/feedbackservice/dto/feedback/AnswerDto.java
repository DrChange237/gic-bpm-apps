package com.ccabank.feedbackservice.dto.feedback;

import com.ccabank.feedbackservice.entity.Feedback;

import javax.validation.constraints.NotNull;

public class AnswerDto {

    private int id;

    @NotNull(message = "question cannot be null")
    private String question;

    @NotNull(message = "answer cannot be null")
    private String answer;

    @NotNull(message = "feedback cannot be null")
    private FeedbackDto feedback;


    public int getId() {
        return id;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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
}
