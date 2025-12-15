package com.ccabank.paperless.dto.memo;

import com.ccabank.paperless.entity.ApprovalStatus;
import com.ccabank.paperless.entity.ApprovalType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApprovalDto implements Serializable {

    private  String id;

    private String key;

    private String description;

    private int position;

    private String staff;

    private List<String> listStaff;

    private  String role;

    private ApprovalType type;

    private LocalDateTime approvalDate;

    private ApprovalStatus status;

    private String comments;

    private String unity;

    private List<FieldDto> fields;

    private Boolean multiple;

    //private RequestInfo request;

    private String time;

    private boolean haveSignature;

    private boolean required;

    public Boolean getMultiple() {
        if(multiple == null){
            return false;
        }
        return multiple;
    }
}
