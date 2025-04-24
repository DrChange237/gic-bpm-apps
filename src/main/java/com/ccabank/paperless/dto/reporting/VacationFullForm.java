package com.ccabank.paperless.dto.reporting;

public class VacationFullForm {
    VacationForm vacation = new VacationForm();

    HandOverForm handover = new HandOverForm();

    public VacationForm getVacation() {
        return vacation;
    }

    public void setVacation(VacationForm vacation) {
        this.vacation = vacation;
    }

    public HandOverForm getHandover() {
        return handover;
    }

    public void setHandover(HandOverForm handover) {
        this.handover = handover;
    }
}
