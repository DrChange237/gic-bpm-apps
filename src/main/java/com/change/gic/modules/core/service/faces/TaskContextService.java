package com.change.gic.modules.core.service.faces;

import com.change.gic.modules.core.info.TaskDetailsResponse;

public interface TaskContextService {
    TaskDetailsResponse getTaskFullContext(String taskId);
}
