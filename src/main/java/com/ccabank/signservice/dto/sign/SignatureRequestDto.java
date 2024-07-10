package com.ccabank.signservice.dto.sign;

import java.util.Collection;
import java.util.List;

public class SignatureRequestDto {

    private Collection<SignDocumentDto> documents;


    public Collection<SignDocumentDto> getDocuments() {
        return documents;
    }

    public void setDocuments(Collection<SignDocumentDto> documents) {
        this.documents = documents;
    }
}
