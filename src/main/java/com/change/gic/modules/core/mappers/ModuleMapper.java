package com.change.gic.modules.core.mappers;

import com.change.gic.modules.core.info.ModuleInfo;
import com.change.gic.modules.core.entity.Module;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ModuleMapper extends EntityMapper<ModuleInfo, Module>{
}
