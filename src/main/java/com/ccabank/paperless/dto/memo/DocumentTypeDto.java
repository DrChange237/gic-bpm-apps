package com.ccabank.paperless.dto.memo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentTypeDto implements Serializable {
     private String name;
     private  String description;
     private DocumentStructure structure;
}
