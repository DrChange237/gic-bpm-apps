package com.ccabank.paperless.dto.memo;

import com.ccabank.paperless.entity.RequestStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestDto implements Serializable {

    private Long id;

    private  String reference;

    private String staff;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime lastModification;

    private int approbationLevel;

    private String documentType;

    private RequestStatus status;

    private List<ApprovalDto> approvals;

    private List<FieldDto> fields;

    private List<FileDto> files;
}
