package com.ccabank.memoservice.mappers;

import com.ccabank.memoservice.dto.memo.ProcessUnityDto;
import com.ccabank.memoservice.dto.memo.RequestDto;
import com.ccabank.memoservice.entity.ProcessUnity;
import com.ccabank.memoservice.entity.Request;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProcessUnityMapper extends EntityMapper<ProcessUnityDto, ProcessUnity> {
}
