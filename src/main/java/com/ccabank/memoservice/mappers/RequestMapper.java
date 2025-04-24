package com.ccabank.memoservice.mappers;

import com.ccabank.memoservice.dto.memo.RequestInfo;
import com.ccabank.memoservice.entity.Request;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface RequestMapper extends EntityMapper<RequestInfo, Request> {


}
