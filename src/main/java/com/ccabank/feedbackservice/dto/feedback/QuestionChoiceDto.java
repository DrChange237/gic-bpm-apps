package com.ccabank.feedbackservice.dto.feedback;

public class QuestionChoiceDto {
    private  String label;

    private String  value;

    private String subQuestions;

    public String getSubQuestions() {
        return subQuestions;
    }

    public void setSubQuestions(String subQuestions) {
        this.subQuestions = subQuestions;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
