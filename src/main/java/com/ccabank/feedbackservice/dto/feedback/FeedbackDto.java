package com.ccabank.feedbackservice.dto.feedback;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;

@ApiModel()
public class FeedbackDto {

    private String id;

    private StaffDto staff;


    @Size(min = 1, max = 150)
    private String form;


    @NotNull(message = "createdAt  cannot be null")
    private LocalDateTime createdAt;


    @NotNull(message = "fullname cannot be null")
    @Size(min = 1, max = 150)
    private String fullname;

    @NotNull(message = "mobile cannot be null")
    @Size(min = 1, max = 15)
    private String mobile;

    @Size(min = 1, max = 150)
    private String email;

    private Collection<AnswerDto> answerCollection;

    public Collection<AnswerDto> getAnswerCollection() {
        return answerCollection;
    }

    public void setAnswerCollection(Collection<AnswerDto> answerCollection) {
        this.answerCollection = answerCollection;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public StaffDto getStaff() {
        return staff;
    }

    public void setStaff(StaffDto staff) {
        this.staff = staff;
    }
}
