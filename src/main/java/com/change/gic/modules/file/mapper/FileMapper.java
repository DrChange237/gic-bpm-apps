package com.change.gic.modules.file.mapper;

import com.change.gic.modules.file.dto.FileDto;
import com.change.gic.modules.file.entity.File;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper extends EntityMapper<FileDto, File>{

}
