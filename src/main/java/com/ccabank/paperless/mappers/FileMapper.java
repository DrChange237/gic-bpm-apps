package com.ccabank.paperless.mappers;

import com.ccabank.paperless.dto.memo.FileDto;
import com.ccabank.paperless.entity.File;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper extends EntityMapper<FileDto, File> {
}
