package com.ccabank.paperless.dto.memo;

import com.ccabank.paperless.entity.RequestStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Data
public class RequestInfo implements Serializable {
    private Long id;

    private  String reference;

    private String staff;

    private LocalDateTime createdAt;

    private int approbationLevel;

    private String documentType;

    private RequestStatus status;

    private List<FieldDto> fields;

    private List<FileDto> files;

    private List<ApprovalDto> approvals;

    private String comments;
}
