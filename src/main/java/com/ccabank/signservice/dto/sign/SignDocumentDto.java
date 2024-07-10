package com.ccabank.signservice.dto.sign;

import com.ccabank.signservice.entity.Document;

import java.util.List;

public class SignDocumentDto {

    private String fileId;

    private List<SignatureDto> signatures;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public List<SignatureDto> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<SignatureDto> signatures) {
        this.signatures = signatures;
    }
}
