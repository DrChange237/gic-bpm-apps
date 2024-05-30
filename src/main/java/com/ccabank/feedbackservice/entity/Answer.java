package com.ccabank.feedbackservice.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "T_ANSWER")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @JoinColumn(name = "FEEDBACK", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Feedback feedback;

    @Basic(optional = false)
    @NotNull
    @Column(name = "QUESTION")
    private String question;

    @Column(name = "ANSWER")
    @Size(max = 1000)
    private String answer;


    public Long getId() {
        return id;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
