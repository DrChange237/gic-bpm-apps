package com.ccabank.paperless.report.scheduled;


import com.ccabank.paperless.report.faces.ExportVacationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;


@Slf4j
@Profile("dev")
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledRH {

    private final ExportVacationService exportVacationService;

    @Scheduled(cron = "0 */3 * * * ?")
    public void exportVacation() throws IOException {
        log.info("Exporting vacation...");
        exportVacationService.exportVacation();
    }

}


