package com.ccabank.memoservice.dto.memo;

public class ArchivageDto {

    private Long id ;

    private Boolean decision;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDecision() {
        return decision;
    }

    public void setDecision(Boolean decision) {
        this.decision = decision;
    }
}
