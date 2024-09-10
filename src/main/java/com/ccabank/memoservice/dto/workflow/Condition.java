package com.ccabank.memoservice.dto.workflow;

public class Condition {

    private Logical logical;

    private ComparisonDetails comparison;

    public Logical getLogical() {
        return logical;
    }

    public void setLogical(Logical logical) {
        this.logical = logical;
    }

    public ComparisonDetails getComparison() {
        return comparison;
    }

    public void setComparison(ComparisonDetails comparison) {
        this.comparison = comparison;
    }
}
