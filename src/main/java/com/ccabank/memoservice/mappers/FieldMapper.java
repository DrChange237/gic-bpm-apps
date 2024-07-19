package com.ccabank.memoservice.mappers;

import com.ccabank.memoservice.dto.memo.FieldDto;
import com.ccabank.memoservice.dto.memo.ProcessUnityDto;
import com.ccabank.memoservice.entity.Field;
import com.ccabank.memoservice.entity.ProcessUnity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface FieldMapper extends EntityMapper<FieldDto, Field> {
}
