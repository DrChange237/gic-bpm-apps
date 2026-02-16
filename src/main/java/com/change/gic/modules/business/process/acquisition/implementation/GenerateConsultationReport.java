package com.change.gic.modules.business.process.acquisition.implementation;

import com.change.gic.modules.business.entity.Consultation;
import com.change.gic.modules.business.service.faces.PDFGenerationService;
import com.change.gic.modules.core.entity.Document;
import com.change.gic.modules.core.repository.DocumentRepository;
import com.change.gic.modules.file.dto.FileDto;
import com.change.gic.modules.file.entity.File;
import com.change.gic.modules.file.repository.FileRepository;
import com.change.gic.modules.file.service.FileService;
import com.change.gic.modules.file.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenerateConsultationReport implements JavaDelegate {

    private final PDFGenerationService pdfGenerationService;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final DocumentRepository documentRepository;

    @Override
    @Transactional
    public void execute(DelegateExecution delegateExecution) throws Exception {

        Consultation consultation = (Consultation) delegateExecution.getVariable("consultation");
        byte[] file = pdfGenerationService.generateConsultationReport(consultation);
        MultipartFile multipartFile = FileUtil.bytesToMultipartFile(file, "customer-profile", "customer-profile.pdf", "application/pdf");
        FileDto fileDto = fileService.uploadFileToDatabase("gic", multipartFile);
        File file1 = fileRepository.getOne(fileDto.getId());
        Document document = new Document();
        document.setLabel("Rapport de Consultation - " + consultation.getInscription().getFirstName() + " " + consultation.getInscription().getLastName());
        document.setFile(file1);
        document.setBusinessKey(delegateExecution.getBusinessKey());
        document.setTag("consultation-report");
        documentRepository.save(document);

    }

}
