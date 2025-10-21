package com.ccabank.paperless.report.scheduled;


import com.ccabank.paperless.report.implementation.ExportVacationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.Date;


@Slf4j
@Profile("dev")
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledRH {

    private ExportVacationService exportVacationService;

    @Scheduled(cron = "0 */2 * * * ?")
    public void exportVacation() throws IOException {
        log.info("Exporting vacation...");
        exportVacationService.exportVacation();
    }


}


