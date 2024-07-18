package com.ccabank.memoservice.dto.memo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentTypeDto {

     private String name;

     private  String description;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private DocumentStructure structure;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DocumentStructure getStructure() {
        return structure;
    }

    public void setStructure(DocumentStructure structure) {
        this.structure = structure;
    }
}
