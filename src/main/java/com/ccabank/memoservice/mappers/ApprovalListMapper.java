package com.ccabank.memoservice.mappers;

import com.ccabank.memoservice.dto.memo.ApprovalDto;
import com.ccabank.memoservice.dto.memo.ApprovalListDto;
import com.ccabank.memoservice.entity.Approval;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApprovalListMapper extends EntityMapper<ApprovalListDto, Approval> {
}
