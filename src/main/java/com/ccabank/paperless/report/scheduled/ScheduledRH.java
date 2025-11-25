package com.ccabank.paperless.report.scheduled;


import com.ccabank.paperless.report.faces.ExportAbsenceService;
import com.ccabank.paperless.report.faces.ExportMissionService;
import com.ccabank.paperless.report.faces.ExportResumptionService;
import com.ccabank.paperless.report.faces.ExportVacationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;


@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledRH {

    private final ExportVacationService exportVacationService;
    private final ExportMissionService exportMissionService;
    private final ExportAbsenceService exportAbsenceService;
    private final ExportResumptionService exportResumptionService;

    @Scheduled(cron = "0 /2 * 21-31 * ?")
    public void exportVacation() throws IOException {
        log.info("Exporting vacation...");
        exportVacationService.exportVacation();
    }

    @Scheduled(cron = "0 /5 23 21-31 * ?")
    public void exportMission() throws IOException {
        log.info("Exporting mission...");
        exportMissionService.exportMission();
    }

    @Scheduled(cron = "0 /5 23 21-31 * ?")
    public void exportAbsence() throws IOException {
        log.info("Exporting absence...");
        exportAbsenceService.exportAbsence();
    }

    @Scheduled(cron = "0 /5 23 21-31 * ?")
    public void exportResumption() throws IOException {
        log.info("Exporting resumption...");
        exportResumptionService.exportResumption();
    }

}


