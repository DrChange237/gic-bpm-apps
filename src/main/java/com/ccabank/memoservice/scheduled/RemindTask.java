package com.ccabank.memoservice.scheduled;


import com.ccabank.memoservice.service.faces.ApprovalService;
import com.ccabank.memoservice.service.faces.CamundaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.task.Task;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class RemindTask {

    private final ApprovalService approvalService;
    private final CamundaService camundaService;

   // @Scheduled(cron = "0 0 9,12,15 * * 1-6")
    @Scheduled(cron = "0 0 8 * * 1-6")
    public void remindStaffForValidationTask() {
        log.info("Remind tasks for validation :: Execution Time - {} ", new Date());
        List<Task> tasks = new ArrayList<>();
        tasks = camundaService.getAllTasksForUser();
        for (Task task : tasks) {
            approvalService.relanceApprobation(task.getId());
        }
    }

}
