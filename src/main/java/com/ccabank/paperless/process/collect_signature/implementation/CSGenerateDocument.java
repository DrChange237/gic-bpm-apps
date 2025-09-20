package com.ccabank.paperless.process.collect_signature.implementation;

import com.ccabank.paperless.dto.memo.FileDto;
import com.ccabank.paperless.dto.reporting.CollectSignatureForm;
import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.dto.user.UserRestDto;
import com.ccabank.paperless.entity.Approbation;
import com.ccabank.paperless.entity.ApprovalStatus;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.openfeign.ReportingRestClient;
import com.ccabank.paperless.openfeign.UserRestClient;
import com.ccabank.paperless.process.general.service.RequestService;
import com.ccabank.paperless.repository.ApprobationRepository;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.FileService;
import com.ccabank.paperless.util.CustomMultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CSGenerateDocument implements JavaDelegate {


    private final ApprobationRepository approbationRepository;
    private final RequestRepository requestRepository;
    private final UserRestClient userRestClient;
    private final ReportingRestClient reportingRestClient;
    private final RequestService requestService;
    private final FileService fileService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Inside CSGenerateDocument");

        Request request = requestRepository.findByInstanceId(delegateExecution.getProcessInstanceId());
        List<Approbation> approbations = approbationRepository.findByReferenceAndStatusOrderByCreationDateDesc(request.getReference(), ApprovalStatus.ACCEPTED);
        CollectSignatureForm form = new CollectSignatureForm();
        form.setReference(request.getReference());
        List<CollectSignatureForm.Signatory> signatories = new ArrayList<>();
        for (Approbation approbation : approbations) {
            CollectSignatureForm.Signatory formSignatory = new CollectSignatureForm.Signatory();
            EmployeeInfo employee = userRestClient.getStaffByUsername(approbation.getStaff());
            String signature = userRestClient.getEmployeeSignature(approbation.getStaff());
            formSignatory.setName(employee.getFirstName() + " " + employee.getLastName());
            formSignatory.setFunction(employee.getFunction().getFunction().getName());
            formSignatory.setComments(approbation.getComments());
            formSignatory.setSignature(signature);
            signatories.add(formSignatory);
        }
        form.setSignatories(signatories);

        log.info(form.toString());
        ByteArrayResource resource = reportingRestClient.signature(form);
        log.info("Signature generated");

        CustomMultipartFile multipartFile = new CustomMultipartFile(resource.getByteArray(), "collecte_signature_" + delegateExecution.getBusinessKey() + ".pdf", "application/pdf");
        FileDto fileDto = new FileDto();
        fileDto.setAddDate(LocalDateTime.now());
        fileDto.setName(request.getType().getName());
        fileDto.setFile(Base64.getEncoder().encodeToString(resource.getByteArray()));
        fileDto.setMultipartFile(multipartFile);
        fileDto.setType("application/pdf");
        request = requestService.confirmRequest(delegateExecution.getProcessInstanceId());
        fileService.saveFile(request, fileDto);

        log.info("File generated");
    }
}
