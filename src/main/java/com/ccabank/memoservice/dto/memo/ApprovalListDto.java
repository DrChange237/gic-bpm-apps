package com.ccabank.memoservice.dto.memo;

import com.ccabank.memoservice.entity.ApprovalStatus;
import com.ccabank.memoservice.entity.ApprovalType;
import lombok.Data;

import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class ApprovalListDto implements Serializable {

    private  String id;

    @Null
    private int position;

    @Null
    private String staff;

    @Null
    private  String role;

    @Null
    private ApprovalType type;

    @Null
    private LocalDateTime approvalDate;

    @Null
    private ApprovalStatus status;

    @Null
    private String comments;

    @Null
    private String unity;

    @Null
    private List<FieldDto> fields;

    @Null
    private RequestInfo request;

    private int priority;

    private Date dueDate;

    private boolean required;

}
