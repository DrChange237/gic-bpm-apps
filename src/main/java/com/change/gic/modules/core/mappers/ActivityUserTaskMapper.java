package com.change.gic.modules.core.mappers;

import com.change.gic.modules.core.entity.ActivityUserTask;
import com.change.gic.modules.core.info.ActivityUserTaskInfo;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ActivityUserTaskMapper  extends EntityMapper<ActivityUserTaskInfo, ActivityUserTask> {
}
