package com.ccabank.feedbackservice.dto.feedback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EvaluationPeriodStaffDto {

    private String username ;

    private LocalDate startAt;

    private LocalDate endAt;

    private List<EvaluationItem> evaluations = new ArrayList<>();;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDate startAt) {
        this.startAt = startAt;
    }

    public LocalDate getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDate endAt) {
        this.endAt = endAt;
    }

    public List<EvaluationItem> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<EvaluationItem> evaluations) {
        this.evaluations = evaluations;
    }
}
