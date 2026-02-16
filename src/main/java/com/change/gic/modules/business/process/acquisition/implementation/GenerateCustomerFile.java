package com.change.gic.modules.business.process.acquisition.implementation;

import com.change.gic.modules.business.dto.CustomerProfileDto;
import com.change.gic.modules.business.entity.Inscription;
import com.change.gic.modules.business.service.faces.PDFGenerationService;
import com.change.gic.modules.core.entity.Document;
import com.change.gic.modules.core.repository.DocumentRepository;
import com.change.gic.modules.core.util.DateUtils;
import com.change.gic.modules.file.dto.FileDto;
import com.change.gic.modules.file.entity.File;
import com.change.gic.modules.file.repository.FileRepository;
import com.change.gic.modules.file.service.FileService;
import com.change.gic.modules.file.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenerateCustomerFile implements JavaDelegate {

    private final PDFGenerationService pdfGenerationService;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final DocumentRepository documentRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        log.info(("GenerateCustomerFile"));

        Inscription inscription = (Inscription) delegateExecution.getVariable("inscription");

        byte[] file = pdfGenerationService.generateCustomerProfilePdf(inscription);
        MultipartFile multipartFile = FileUtil.bytesToMultipartFile(file, "customer-profile", "customer-profile.pdf", "application/pdf");
        FileDto fileDto = fileService.uploadFileToDatabase("gic", multipartFile);
        File file1 = fileRepository.getOne(fileDto.getId());
        Document document = new Document();
        document.setLabel("Fiche Client - " + inscription.getFirstName() + " " + inscription.getLastName());
        document.setFile(file1);
        document.setBusinessKey(delegateExecution.getBusinessKey());
        document.setTag("customer-profile");
        documentRepository.save(document);
    }
}
