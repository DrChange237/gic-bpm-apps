package com.ccabank.paperless.entity;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "T_FILE")
public class File extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @JoinColumn(name = "REQUEST", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private Request request;

    @Column(name = "URL")
    @Size(max = 255)
    private String url;

    @Column(name = "NAME")
    @Size(max = 255)
    private String name;

    @Column(name = "TYPE")
    @Size(max = 255)
    private String type;

    @Column(name = "ADD_DATE", nullable = true)
    private LocalDateTime addDate;

    @Column(name = "AUTHORIZED", nullable = true)
    @Size(max = 1000)
    private String authorized;

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public @Size(max = 1000) String getAuthorized() {
        return authorized;
    }

    public void setAuthorized(@Size(max = 1000) String authorized) {
        this.authorized = authorized;
    }

    public LocalDateTime getAddDate() {
        return addDate;
    }

    public void setAddDate(LocalDateTime addDate) {
        this.addDate = addDate;
    }
}
