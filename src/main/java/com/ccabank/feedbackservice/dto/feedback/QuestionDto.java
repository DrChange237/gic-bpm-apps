package com.ccabank.feedbackservice.dto.feedback;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class QuestionDto {

    private String property;

    private String label;

    private String type;

    private String value;

    private  boolean api;

    private String url;

    private boolean sort;

    private boolean haveSubQuestions = false;

    private String yesQuestions;

    private String noQuestions;


    private List<QuestionChoiceDto> choices;

    public boolean isSort() {
        return sort;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }

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
        if (this.sort){
            //return this.choices.stream().sorted();
            this.choices.sort(Comparator.comparing(QuestionChoiceDto::getLabel));
            return this.choices;

        }
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

    public boolean isApi() {
        return api;
    }

    public void setApi(boolean api) {
        this.api = api;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isHaveSubQuestions() {
        return haveSubQuestions;
    }

    public void setHaveSubQuestions(boolean haveSubQuestions) {
        this.haveSubQuestions = haveSubQuestions;
    }

    public String getYesQuestions() {
        return yesQuestions;
    }

    public void setYesQuestions(String yesQuestions) {
        this.yesQuestions = yesQuestions;
    }

    public String getNoQuestions() {
        return noQuestions;
    }

    public void setNoQuestions(String noQuestions) {
        this.noQuestions = noQuestions;
    }
}
