package com.ccabank.memoservice.dto.memo;


import com.ccabank.memoservice.util.field.FieldUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldDto {

    private String key;

    private String value;

    private String  name;

    private String type;

    private List<ChoiceDto> choices;

    private List<FileDto> files;


    private boolean required = true;

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<ChoiceDto> getChoices() {
        return choices;
    }

    public void setChoices(List<ChoiceDto> choices) {
        this.choices = choices;
    }

    public List<FileDto> getFiles() {
        return files;
    }

    public void setFiles(List<FileDto> files) {
        this.files = files;
    }
}
