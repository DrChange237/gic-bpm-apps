package com.ccabank.feedbackservice.dto.feedback;

public class EvaluationItem {

    private String  element;

    private int count;

    private float pourcent;

    public String getElement() {
        return element;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public float getPourcent() {
        return pourcent;
    }

    public void setPourcent(float pourcent) {
        this.pourcent = pourcent;
    }
}
