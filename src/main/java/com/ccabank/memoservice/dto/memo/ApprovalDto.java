package com.ccabank.memoservice.dto.memo;

import com.ccabank.memoservice.entity.ApprovalStatus;
import com.ccabank.memoservice.entity.ApprovalType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApprovalDto {

    private  Long id;

    private String key;

    private int position;

    private String staff;

    private  String role;

    private ApprovalType type;

    private LocalDateTime approvalDate;

    private ApprovalStatus status;

    private String comments;

    private String unity;

    private List<FieldDto> fields;

    private RequestInfo request;

    private boolean required;

}
