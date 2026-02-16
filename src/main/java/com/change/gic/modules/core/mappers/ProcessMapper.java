package com.change.gic.modules.core.mappers;

import com.change.gic.modules.core.entity.Process;
import com.change.gic.modules.core.info.ProcessInfo;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ProcessMapper  extends EntityMapper<ProcessInfo, Process> {
}
