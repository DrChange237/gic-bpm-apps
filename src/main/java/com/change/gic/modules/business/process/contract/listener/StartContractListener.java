package com.change.gic.modules.business.process.contract.listener;

import com.change.gic.exception.BadRequestException;
import com.change.gic.modules.business.entity.Consultation;
import com.change.gic.modules.business.entity.ContratTerm;
import com.change.gic.modules.business.entity.ContratTermGroup;
import com.change.gic.modules.business.entity.Inscription;
import com.change.gic.modules.business.enumeration.InscriptionStatus;
import com.change.gic.modules.business.enumeration.Matrimonial;
import com.change.gic.modules.business.repository.ConsultationRepository;
import com.change.gic.modules.business.repository.InscriptionRepository;
import com.change.gic.modules.core.dto.camunda.form.SelectOptionDto;
import com.change.gic.modules.core.repository.ContratTermGroupRepository;
import com.change.gic.modules.core.repository.ContratTermRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartContractListener implements ExecutionListener {

    private final ConsultationRepository consultationRepository;
    private final ContratTermGroupRepository contratTermGroupRepository;
    private final ContratTermRepository contratTermRepository;
    private final InscriptionRepository inscriptionRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String reference = (String) delegateExecution.getVariable("contract_customerRef");
        Inscription inscription = inscriptionRepository.findByReferenceAndStatus(reference, InscriptionStatus.CONSULTED);

        if(inscription == null) {
            throw new BadRequestException("inscription reference does not exist");
        }

        delegateExecution.setProcessBusinessKey(reference);

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

        int age = Period.between(inscription.getBirthday(), LocalDate.now()).getYears();
        delegateExecution.setVariable("age", age);

        if(inscription.getBirthdayConjoint() != null){
            int ageConjoint = Period.between(inscription.getBirthdayConjoint(), LocalDate.now()).getYears();
            delegateExecution.setVariable("ageConjoint", ageConjoint);
        }





        log.info("Inscription Customer saved successfully");

        // ------------------ CONJOINT -------------------------------------------------------------

        delegateExecution.setVariable("firstNameConjoint", inscription.getFirstNameConjoint());
        delegateExecution.setVariable("lastNameConjoint", inscription.getLastNameConjoint());

        String birthdayConjoint = (String) delegateExecution.getVariable("birthdayConjoint");
        if(!birthdayConjoint.isEmpty()){
            delegateExecution.setVariable("birthdayConjoint", inscription.getBirthdayConjoint());
        }

        delegateExecution.setVariable("birthplaceConjoint", inscription.getBirthplaceConjoint());
        delegateExecution.setVariable("nationalityConjoint", inscription.getNationalityConjoint());
        delegateExecution.setVariable("mobileConjoint", inscription.getMobileConjoint());
        delegateExecution.setVariable("emailConjoint", inscription.getEmailConjoint());
        delegateExecution.setVariable("cniNumberConjoint", inscription.getCniNumberConjoint());

        String cniDeliveryConjoint = (String) delegateExecution.getVariable("cniDeliveryConjoint");
        if(!cniDeliveryConjoint.isEmpty()){
            delegateExecution.setVariable("cniDeliveryConjoint", inscription.getCniDeliveryConjoint());
        }
        delegateExecution.setVariable("cniPlaceConjoint", inscription.getCniPlaceConjoint());
        delegateExecution.setVariable("diplomaConjoint", inscription.getDiplomaConjoint());
        Object value = delegateExecution.getVariable("yearGraduationConjoint");
        if (value instanceof Integer) {
            delegateExecution.setVariable("yearGraduationConjoint", (Integer) value);
        }


        Consultation consultation = consultationRepository.findByInscription(inscription);
        delegateExecution.setVariable("consultation", consultation);

        delegateExecution.setVariable("contract_amount_phase_1_value", consultation.getFirstAmount().longValueExact());
        delegateExecution.setVariable("contract_amount_phase_2_value", consultation.getSecondAmount().longValueExact());
        delegateExecution.setVariable("contract_amount_phase_3_value", consultation.getLastAmount().longValueExact());



        List<SelectOptionDto> selectOptions = new ArrayList<>();

        ContratTermGroup prep_doc = contratTermGroupRepository.findByCode("prep_doc");
        List<ContratTerm> contratTerms = contratTermRepository.findByGroup(prep_doc);
        selectOptions = contratTerms.stream().map(x -> new SelectOptionDto(x.getLabel(), x.getCode())).collect(Collectors.toList());
        delegateExecution.setVariable("term_prep_doc_values", selectOptions);

        ContratTermGroup submit_folder = contratTermGroupRepository.findByCode("submit_folder");
        contratTerms = contratTermRepository.findByGroup(submit_folder);
        selectOptions = contratTerms.stream().map(x -> new SelectOptionDto(x.getLabel(), x.getCode())).collect(Collectors.toList());
        delegateExecution.setVariable("term_submit_folder_values", selectOptions);

        ContratTermGroup submit_rp = contratTermGroupRepository.findByCode("submit_rp");
        contratTerms = contratTermRepository.findByGroup(submit_rp);
        selectOptions = contratTerms.stream().map(x -> new SelectOptionDto(x.getLabel(), x.getCode())).collect(Collectors.toList());
        delegateExecution.setVariable("term_submit_rp_values", selectOptions);


        ContratTermGroup biometry = contratTermGroupRepository.findByCode("biometry");
        contratTerms = contratTermRepository.findByGroup(biometry);
        selectOptions = contratTerms.stream().map(x -> new SelectOptionDto(x.getLabel(), x.getCode())).collect(Collectors.toList());
        delegateExecution.setVariable("term_biometry_values", selectOptions);


        ContratTermGroup visa = contratTermGroupRepository.findByCode("visa");
        contratTerms = contratTermRepository.findByGroup(visa);
        selectOptions = contratTerms.stream().map(x -> new SelectOptionDto(x.getLabel(), x.getCode())).collect(Collectors.toList());
        delegateExecution.setVariable("term_visa_values", selectOptions);

        ContratTermGroup integration = contratTermGroupRepository.findByCode("integration");
        contratTerms = contratTermRepository.findByGroup(integration);
        selectOptions = contratTerms.stream().map(x -> new SelectOptionDto(x.getLabel(), x.getCode())).collect(Collectors.toList());
        delegateExecution.setVariable("term_integration_values", selectOptions);

        ContratTermGroup fees = contratTermGroupRepository.findByCode("fees");
        contratTerms = contratTermRepository.findByGroup(fees);
        selectOptions = contratTerms.stream().map(x -> new SelectOptionDto(x.getLabel(), x.getCode())).collect(Collectors.toList());
        delegateExecution.setVariable("term_fees_values", selectOptions);

    }
}
