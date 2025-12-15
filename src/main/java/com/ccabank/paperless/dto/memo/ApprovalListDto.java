package com.ccabank.paperless.dto.memo;

import com.ccabank.paperless.entity.ApprovalStatus;
import com.ccabank.paperless.entity.ApprovalType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class ApprovalListDto implements Serializable {

    private  String id;
    private int position;
    private String staff;
    private  String role;
    private ApprovalType type;
    private LocalDateTime approvalDate;
    private ApprovalStatus status;
    private String comments;
    private String unity;
    private List<FieldDto> fields;
    private String time;
    private RequestInfo request;
    private int priority;
    private Date dueDate;
    private boolean required;

}
