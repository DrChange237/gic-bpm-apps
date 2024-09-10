package com.ccabank.memoservice.dto.workflow;

public class Transition {

    private int to;

    private Condition condition;

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
