package com.change.gic.modules.business.process.document.listener;

import com.change.gic.exception.BadRequestException;
import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.DocumentType;
import com.change.gic.modules.business.entity.Inscription;
import com.change.gic.modules.business.repository.ContratRepository;
import com.change.gic.modules.business.repository.DocumentTypeRepository;
import com.change.gic.modules.core.dto.camunda.form.SelectOptionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartAddDocumentListener implements ExecutionListener {

    private final ContratRepository contratRepository;
    private final DocumentTypeRepository documentTypeRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        log.warn("START", "start Document");

        String reference = (String) delegateExecution.getVariable("reference");
        log.info(reference);
        if(reference == null){
            reference = delegateExecution.getBusinessKey();
        }

        Contrat contrat = contratRepository.findByReference(reference);
        if(contrat == null) {
            throw new BadRequestException("contrat reference does not exist");
        }

        Inscription inscription = contrat.getConsultation().getInscription();
        delegateExecution.setVariable("firstName", inscription.getFirstName());
        delegateExecution.setVariable("lastName", inscription.getLastName());
        delegateExecution.setVariable("birthday", inscription.getBirthday());
        delegateExecution.setVariable("birthplace", inscription.getBirthplace());
        delegateExecution.setVariable("sexe", inscription.getSexe());
        delegateExecution.setVariable("nationality", inscription.getNationality());
        delegateExecution.setVariable("matrimonial", inscription.getMatrimonial());
        delegateExecution.setVariable("mobile", inscription.getMobile());
        delegateExecution.setVariable("email", inscription.getEmail());
        delegateExecution.setVariable("address", inscription.getAddress());
        delegateExecution.setVariable("cniNumber", inscription.getCniNumber());
        delegateExecution.setVariable("cniDelivery", inscription.getCniDelivery());
        delegateExecution.setVariable("cniPlace", inscription.getCniPlace());
        delegateExecution.setVariable("diploma", inscription.getDiploma());
        delegateExecution.setVariable("yearGraduation", inscription.getYearGraduation());
        delegateExecution.setVariable("school", inscription.getSchool());
        delegateExecution.setVariable("formation", inscription.getFormation());
        delegateExecution.setVariable("children", inscription.getChildren());
        delegateExecution.setVariable("childrenAge", inscription.getChildrenAge());
        delegateExecution.setVariable("experience", inscription.getExperience());

        log.info(reference);
        delegateExecution.setProcessBusinessKey(reference);

        String customer = inscription.getFullName();
        delegateExecution.setVariable("customer_value", customer);

        List<DocumentType> documentTypes = documentTypeRepository.findAll();
        List<SelectOptionDto> selectOptionDtos = documentTypes.stream().map(x -> new SelectOptionDto(x.getName(), x.getTag())).collect(Collectors.toList());
        delegateExecution.setVariable("document_type_values", selectOptionDtos);

    }

}
