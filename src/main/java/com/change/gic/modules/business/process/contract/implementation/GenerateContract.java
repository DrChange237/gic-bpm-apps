package com.change.gic.modules.business.process.contract.implementation;

import com.change.gic.modules.business.entity.Consultation;
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

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenerateContract implements JavaDelegate {

    private final ContratRepository contratRepository;
    private final ContratTermRepository contratTermRepository;
    private final ContratTermGroupRepository contratTermGroupRepository;
    private final PDFGenerationService pdfGenerationService;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final DocumentRepository  documentRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        log.info("Generate contract process");
        Contrat contrat = new Contrat();
        Consultation consultation = (Consultation) delegateExecution.getVariable("consultation");
        contrat.setConsultation(consultation);
        contrat.setArchived(false);
        contrat.setStatus(ContratStatus.DRAFT);
        String reference = (String) delegateExecution.getVariable("contract_customerRef");
        contrat.setReference(reference);

        Integer amount1 = (Integer) delegateExecution.getVariable("contract_amount_phase_1");
        Integer amount2 = (Integer) delegateExecution.getVariable("contract_amount_phase_2");
        Integer amount3 = (Integer) delegateExecution.getVariable("contract_amount_phase_3");

        contrat.setFirstAmount(BigDecimal.valueOf(amount1));
        contrat.setSecondAmount(BigDecimal.valueOf(amount2));
        contrat.setLastAmount(BigDecimal.valueOf(amount3));

        Set<ContratTerm> terms = new HashSet<>();
        Set<ContratTerm> debours = new HashSet<>();


        List<String> term_pre_doc = (List<String>) delegateExecution.getVariable("term_prep_doc");
        for (String term : term_pre_doc) {
            ContratTerm contratTerm = contratTermRepository.findByCode(term);
            if (contratTerm != null) {
                terms.add(contratTerm);
            }
        }
        List<String> term_submit_folder = (List<String>) delegateExecution.getVariable("term_submit_folder");
        for (String term : term_submit_folder) {
            ContratTerm contratTerm = contratTermRepository.findByCode(term);
            if (contratTerm != null) {
                terms.add(contratTerm);
            }
        }

        List<String> term_submit_rp = (List<String>) delegateExecution.getVariable("term_submit_rp");
        for (String term : term_submit_rp) {
            ContratTerm contratTerm = contratTermRepository.findByCode(term);
            if (contratTerm != null) {
                terms.add(contratTerm);
            }
        }

        List<String> term_biometry = (List<String>) delegateExecution.getVariable("term_biometry");
        for (String term : term_biometry) {
            ContratTerm contratTerm = contratTermRepository.findByCode(term);
            if (contratTerm != null) {
                terms.add(contratTerm);
            }
        }

        List<String> term_visa = (List<String>) delegateExecution.getVariable("term_visa");
        for (String term : term_visa) {
            ContratTerm contratTerm = contratTermRepository.findByCode(term);
            if (contratTerm != null) {
                terms.add(contratTerm);
            }
        }

        List<String> term_integration = (List<String>) delegateExecution.getVariable("term_integration");
        for (String term : term_integration) {
            ContratTerm contratTerm = contratTermRepository.findByCode(term);
            if (contratTerm != null) {
                terms.add(contratTerm);
            }
        }

        List<String> term_fees = (List<String>) delegateExecution.getVariable("term_fees");
        for (String term : term_fees) {
            ContratTerm contratTerm = contratTermRepository.findByCode(term);
            if (contratTerm != null) {
                debours.add(contratTerm);
            }
        }

        contrat.setTerms(terms);
        contrat.setDebours(debours);
        contratRepository.save(contrat);

        contrat.setTerms(terms);
        contratRepository.save(contrat);
        List<ContratTermGroup> contratTermGroups = contratTermGroupRepository.findAllByOrderByPositionAsc();
        contratTermGroups.removeIf(c -> c.getCode().equals("fees"));
        List<ContratTerm> contratTerms = terms.stream().sorted(Comparator.comparing(ContratTerm::getPosition)).collect(Collectors.toList());

        ContratTermGroup groupDebours = contratTermGroupRepository.findByCode("fees");
        List<ContratTerm> allDebours = contratTermRepository.findByGroupOrderByPositionAsc(groupDebours);

        byte[] file = pdfGenerationService.generateContract(contrat,contratTermGroups,contratTerms, allDebours, debours.stream().collect(Collectors.toList()) );
        MultipartFile multipartFile = FileUtil.bytesToMultipartFile(file, "contract", "contract.pdf", "application/pdf");
        try{
            FileDto fileDto = fileService.uploadFileToDatabase("gic", multipartFile);
            File file1 = fileRepository.getOne(fileDto.getId());
            Document document = new Document();
            document.setLabel("Ebauche Contrat - " + contrat.getConsultation().getInscription().getFullName());
            document.setFile(file1);
            document.setBusinessKey(delegateExecution.getBusinessKey());
            document.setTag("contract");
            documentRepository.save(document);
        }catch (Exception e){
            log.error(e.getMessage());
        }


    }
}
