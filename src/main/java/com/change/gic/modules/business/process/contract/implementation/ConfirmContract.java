package com.change.gic.modules.business.process.contract.implementation;

import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.ContratTerm;
import com.change.gic.modules.business.entity.ContratTermGroup;
import com.change.gic.modules.business.enumeration.ContratStatus;
import com.change.gic.modules.business.repository.ContratRepository;
import com.change.gic.modules.business.service.faces.PDFGenerationService;
import com.change.gic.modules.core.entity.Document;
import com.change.gic.modules.core.repository.ContratTermGroupRepository;
import com.change.gic.modules.core.repository.ContratTermRepository;
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
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConfirmContract implements JavaDelegate {

    private final ContratRepository contratRepository;
    private final ContratTermRepository contratTermRepository;
    private final ContratTermGroupRepository contratTermGroupRepository;
    private final DocumentRepository documentRepository;
    private final FileRepository fileRepository;
    private final PDFGenerationService pdfGenerationService;
    private final FileService fileService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        log.info("Generate confirm contract process");
        String reference = (String) delegateExecution.getBusinessKey();
        Contrat contrat = contratRepository.findByReference(reference);

        contratRepository.save(contrat);
        List<ContratTermGroup> contratTermGroups = contratTermGroupRepository.findAllByOrderByPositionAsc();
        contratTermGroups.removeIf(c -> c.getCode().equals("fees"));
        List<ContratTerm> contratTerms = contrat.getTerms().stream().sorted(Comparator.comparing(ContratTerm::getPosition)).collect(Collectors.toList());

        ContratTermGroup groupDebours = contratTermGroupRepository.findByCode("fees");
        List<ContratTerm> allDebours = contratTermRepository.findByGroupOrderByPositionAsc(groupDebours);

        byte[] file = pdfGenerationService.generateContract(contrat,contratTermGroups,contratTerms, allDebours, contrat.getDebours().stream().collect(Collectors.toList()) );
        MultipartFile multipartFile = FileUtil.bytesToMultipartFile(file, "contract", "contract.pdf", "application/pdf");
        try{
            FileDto fileDto = fileService.uploadFileToDatabase("gic", multipartFile);
            File file1 = fileRepository.getOne(fileDto.getId());
            Document document = new Document();
            document.setLabel("Contrat - " + contrat.getConsultation().getInscription().getFullName());
            document.setFile(file1);
            document.setBusinessKey(delegateExecution.getBusinessKey());
            document.setTag("contract");
            documentRepository.save(document);
        }catch (Exception e){
            log.error(e.getMessage());
        }

        contrat.setStatus(ContratStatus.SIGNED);
        contratRepository.save(contrat);

    }

}
