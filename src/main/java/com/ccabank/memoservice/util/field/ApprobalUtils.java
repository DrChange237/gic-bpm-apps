package com.ccabank.memoservice.util.field;

import com.ccabank.memoservice.dto.memo.ApprovalDto;
import com.ccabank.memoservice.dto.memo.DocumentStructure;
import com.ccabank.memoservice.dto.memo.FieldDto;

import java.util.List;

public class ApprobalUtils {

    public static ApprovalDto getApprobalStructure(String type, int position){

        DocumentStructure structure = FieldUtils.getStructure(type);
        List<ApprovalDto> approvalDtos = structure.getApprovals();

        ApprovalDto approvalDto = approvalDtos.stream().filter(obj -> obj.getPosition() == position).findFirst().get();

        if(approvalDto == null){
            return null;
        }

        return approvalDto;
    }
}
