package com.ccabank.signservice.dto.sign;

public class SignatureDto {

     private String signer;

     private  SignPositionDto position;

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public SignPositionDto getPosition() {
        return position;
    }

    public void setPosition(SignPositionDto position) {
        this.position = position;
    }
}
