package com.change.gic.modules.core.info;


import lombok.Data;

import java.util.Date;

@Data
public class ActivityHistoryInfo {

    private String activityId;
    private String activityName;
    private String activityType;
    private Date startTime;
    private Date endTime;
    private String assignee;
}
