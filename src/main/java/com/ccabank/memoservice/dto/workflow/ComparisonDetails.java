package com.ccabank.memoservice.dto.workflow;

import com.ccabank.memoservice.dto.memo.FieldDto;

public class ComparisonDetails {

    private String operator;

    private FieldDto field;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public FieldDto getField() {
        return field;
    }

    public void setField(FieldDto field) {
        this.field = field;
    }
}
