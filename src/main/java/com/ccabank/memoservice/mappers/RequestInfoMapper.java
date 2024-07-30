package com.ccabank.memoservice.mappers;

import com.ccabank.memoservice.dto.memo.ProcessUnityDto;
import com.ccabank.memoservice.dto.memo.RequestInfo;
import com.ccabank.memoservice.entity.ProcessUnity;
import com.ccabank.memoservice.entity.Request;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RequestInfoMapper extends EntityMapper<RequestInfo, Request> {
}
