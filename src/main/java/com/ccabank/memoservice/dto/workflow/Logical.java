package com.ccabank.memoservice.dto.workflow;

import java.util.List;

public class Logical {

    private String operator;

    private List<Comparison> conditions;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<Comparison> getConditions() {
        return conditions;
    }

    public void setConditions(List<Comparison> conditions) {
        this.conditions = conditions;
    }
}
