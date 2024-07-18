package com.ccabank.memoservice.mappers;

import com.ccabank.memoservice.dto.memo.ApprovalDto;
import com.ccabank.memoservice.dto.memo.RequestDto;
import com.ccabank.memoservice.entity.Approval;
import com.ccabank.memoservice.entity.Request;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ApprovalMapper extends EntityMapper<ApprovalDto, Approval> {

}
