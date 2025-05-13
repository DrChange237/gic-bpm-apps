package com.ccabank.paperless.process.general.listener.execution;

import com.ccabank.paperless.service.faces.ApprovalService;
import com.ccabank.paperless.service.faces.CamundaService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DaylyRemindTaskListener implements ExecutionListener {

    private final CamundaService camundaService;

    private final ApprovalService approvalService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {

        System.out.println("DaylyRemindTaskListener Execution Listener");

        String processInstanceId = execution.getProcessInstanceId();

        List<Task> tasks = camundaService.getActiveTasksByInstance(processInstanceId);

        if (!tasks.isEmpty()) {
            for (Task task : tasks) {
                approvalService.relanceApprobation(task.getId());
            }
        } else {
            System.out.println("Aucune tâche active associée à ce processus.");
        }
    }
}
