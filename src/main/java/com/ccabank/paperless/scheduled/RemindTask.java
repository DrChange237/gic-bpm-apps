package com.ccabank.paperless.scheduled;


import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.entity.RequestStatus;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.ApprovalService;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.task.Task;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Profile("prod")
public class RemindTask {

    private final ApprovalService approvalService;
    private final CamundaService camundaService;
    private final RequestService requestService;
    private final RequestRepository requestRepository;

    @Scheduled(cron = "0 0 8 * * 1-6", zone = "GMT+1")
    public void remindStaffForValidationTask() {
        log.info("Remind tasks for validation :: Execution Time - {} ", new Date());
        List<Task> tasks = new ArrayList<>();
        tasks = camundaService.getAllTasksForUser();
        for (Task task : tasks) {
            approvalService.relanceApprobation(task.getId());
        }
    }

    //@Scheduled(cron = "0 0 15 * * 1-6", zone = "GMT+1")
    public void remindStaffForDueDateTask() {
        log.info("Remind tasks for due date :: Execution Time - {} ", new Date());
        List<Task> tasks = new ArrayList<>();
        tasks = camundaService.getAllTasksForUser();
        for (Task task : tasks) {
            //approvalService.relanceForDueDate(task.getId());
        }
    }

    @Scheduled(cron = "0 0 10 * * 1-5", zone = "GMT+1")
    public void cancelNotValidate() {
        log.info("DRAFT cancelNotValidate :: Execution Time - {} ", new Date());
        List<Request> requests = requestRepository.findByStatus(RequestStatus.DRAFT);
        for (Request request : requests) {
            boolean isOlderThan7Days = request.getCreatedAt().isBefore(LocalDateTime.now().minusDays(7));
            if (isOlderThan7Days) {
                try {
                    requestService.suspend(request.getId(), "Pas de validation depuis plus de 7 jours");
                }catch (Exception e){
                    log.info(e.getMessage());
                }
            }
        }

        log.info("REJECTED cancelNotValidate :: Execution Time - {} ", new Date());
        requests = requestRepository.findByStatus(RequestStatus.REJECTED);
        for (Request request : requests) {
            boolean isOlderThan7Days = request.getCreatedAt().isBefore(LocalDateTime.now().minusDays(10));
            if (isOlderThan7Days) {
                try {
                    requestService.suspend(request.getId(), "Pas de mise à jour depuis plus de 10 jours");
                }catch (Exception e){
                    log.info(e.getMessage());
                }
            }
        }
    }

}
