package com.ccabank.memoservice.dto.email;

public class AttachmentDto {
    private String name;
    private String data;

    public AttachmentDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}


