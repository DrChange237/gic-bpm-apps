package com.ccabank.feedbackservice.dto.feedback;

import java.util.List;

public class QuestionDto {

    private String property;

    private String label;

    private String type;

    private String value;

    private List<QuestionChoiceDto> choices;


    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<QuestionChoiceDto> getChoices() {
        return choices;
    }

    public void setChoices(List<QuestionChoiceDto> choices) {
        this.choices = choices;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
