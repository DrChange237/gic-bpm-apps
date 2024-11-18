package com.ccabank.memoservice.dto.memo;

public class ChoiceDto {

    public ChoiceDto(){

    }

    public ChoiceDto(String label, Object value){
        this.label = label;
        this.value = value;
    }

    private String label;

    private Object value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
