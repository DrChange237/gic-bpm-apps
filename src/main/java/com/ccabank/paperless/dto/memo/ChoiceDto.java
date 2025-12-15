package com.ccabank.paperless.dto.memo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChoiceDto  implements Serializable {

    private String label;
    private Object value;

    public ChoiceDto() {
        // obligatoire pour Jackson
    }

    public ChoiceDto(String label, Object value){
        this.label = label;
        this.value = value;
    }
}
