package com.ccabank.feedbackservice.dto.feedback;

public class CountNoteDto {

    private String note;

    private int count = 0;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
