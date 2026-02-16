package com.change.gic.modules.core.mappers;

import com.change.gic.modules.core.entity.Document;
import com.change.gic.modules.core.info.DocumentInfo;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface DocumentMapper extends EntityMapper<DocumentInfo, Document> {
}
