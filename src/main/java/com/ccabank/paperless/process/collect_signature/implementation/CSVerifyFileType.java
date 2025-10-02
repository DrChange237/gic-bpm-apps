package com.ccabank.paperless.process.collect_signature.implementation;

import com.ccabank.paperless.exception.BadRequestException;
import com.ccabank.paperless.openfeign.FileRestClient;
import com.ccabank.paperless.service.faces.FileService;
import com.ccabank.paperless.util.file.PdfUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CSVerifyFileType implements JavaDelegate {

    private final FileRestClient fileRestClient;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        String file = (String) delegateExecution.getVariable("file");
        log.info("Processing file {}", file);
        String fileBase = fileRestClient.getB64FileById(file);
        if(!PdfUtils.isBase64Pdf(fileBase)) {
            throw new BadRequestException("Le fichier doit ètre un pdf");
        }
    }
}
