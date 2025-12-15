package com.ccabank.paperless.dto.memo;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class FieldDto implements Serializable {

    private Long id;
    private int position;
    private String key;
    private String value;
    private String  name;
    private String type;
    private String description;
    private String ngIf = "true";
    private List<ChoiceDto> choices;
    private List<FileDto> files = new ArrayList<>();
    private boolean required = true;
    private String defaultValue = "";

}
