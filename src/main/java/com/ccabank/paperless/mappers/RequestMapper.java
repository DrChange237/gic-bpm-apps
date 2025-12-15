package com.ccabank.paperless.mappers;

import com.ccabank.paperless.dto.memo.RequestInfo;
import com.ccabank.paperless.entity.Request;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface RequestMapper extends EntityMapper<RequestInfo, Request> {
    @Override
    RequestInfo toDto(Request entity);

    @Override
    Request toEntity(RequestInfo dto);
}
