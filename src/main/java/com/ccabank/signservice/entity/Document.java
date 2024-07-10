package com.ccabank.signservice.entity;

import javax.persistence.*;


@Entity
@Table(name = "T_DOCUMENT")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @JoinColumn(name = "REQUEST", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SignatureRequest request;

    @Column(name = "FILEID", nullable = false)
    private String fileId;

    @Column(name = "FILEPATH", nullable = false)
    private String filePath;

    @Column(name = "FILEID_SIGNED", nullable = false)
    private String fileIdSigned;

    @Column(name = "FILEPATH_SIGNED", nullable = false)
    private String filePathSigned;


    public Long getId() {
        return id;
    }

    public SignatureRequest getRequest() {
        return request;
    }

    public void setRequest(SignatureRequest request) {
        this.request = request;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileIdSigned() {
        return fileIdSigned;
    }

    public void setFileIdSigned(String fileIdSigned) {
        this.fileIdSigned = fileIdSigned;
    }

    public String getFilePathSigned() {
        return filePathSigned;
    }

    public void setFilePathSigned(String filePathSigned) {
        this.filePathSigned = filePathSigned;
    }
}
