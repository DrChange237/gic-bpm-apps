package com.change.gic.modules.business.process.acquisition.implementation;

import com.change.gic.modules.business.entity.Agency;
import com.change.gic.modules.business.entity.Inscription;
import com.change.gic.modules.business.enumeration.InscriptionStatus;
import com.change.gic.modules.business.enumeration.Matrimonial;
import com.change.gic.modules.business.repository.InscriptionRepository;
import com.change.gic.modules.core.entity.AppUser;
import com.change.gic.modules.core.repository.AppUserRepository;
import com.change.gic.modules.core.service.faces.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class SaveInscription implements JavaDelegate {

    private final InscriptionRepository inscriptionRepository;
    private final AuthService authService;
    private final AppUserRepository appUserRepository;

    @Transactional
    public void execute(DelegateExecution execution) throws Exception {

        String username = authService.getCurrentUsername();
        AppUser owner = appUserRepository.findByUsername(username).get();
        Agency agency = owner.getAgency();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy");
        Inscription inscription = new Inscription();
        inscription.setAgency(agency);
        inscription.setFirstName((String) execution.getVariable("firstName"));
        inscription.setLastName((String) execution.getVariable("lastName"));
        inscription.setBirthday(LocalDate.parse((String) execution.getVariable("birthday"), formatter) );
        inscription.setBirthplace((String) execution.getVariable("birthplace"));
        inscription.setSexe((String) execution.getVariable("sexe"));
        inscription.setNationality((String) execution.getVariable("nationality"));
        inscription.setMatrimonial(Matrimonial.valueOf( (String) execution.getVariable("matrimonial")));
        inscription.setMobile((String) execution.getVariable("mobile"));
        inscription.setEmail((String) execution.getVariable("email"));
        inscription.setAddress( (String) execution.getVariable("address"));
        inscription.setCniNumber( (String) execution.getVariable("cniNumber"));
        inscription.setCniDelivery(LocalDate.parse( (String) execution.getVariable("cniDelivery"),formatter) );
        inscription.setCniPlace( (String) execution.getVariable("cniPlace"));
        inscription.setDiploma( (String) execution.getVariable("diploma"));
        inscription.setYearGraduation((Integer) execution.getVariable("yearGraduation"));
        inscription.setSchool( (String) execution.getVariable("school"));
        inscription.setFormation( (String) execution.getVariable("formation"));
        inscription.setChildren((Integer) execution.getVariable("children"));
        inscription.setChildrenAge((String) execution.getVariable("childrenAge"));
        inscription.setExperience((Integer) execution.getVariable("experience"));
        inscription.setStatus(InscriptionStatus.DRAFT);
        inscription.setReference(execution.getBusinessKey());
        inscription = inscriptionRepository.save(inscription);

        int age = Period.between(inscription.getBirthday(), LocalDate.now()).getYears();
        execution.setVariable("age", age);

        if(inscription.getBirthdayConjoint() != null){
            int ageConjoint = Period.between(inscription.getBirthdayConjoint(), LocalDate.now()).getYears();
            execution.setVariable("ageConjoint", ageConjoint);
        }





        log.info("Inscription Customer saved successfully");

        // ------------------ CONJOINT -------------------------------------------------------------

        inscription.setFirstNameConjoint((String) execution.getVariable("firstNameConjoint"));
        inscription.setLastNameConjoint((String) execution.getVariable("lastNameConjoint"));

        String birthdayConjoint = (String) execution.getVariable("birthdayConjoint");
        if(!birthdayConjoint.isEmpty()){
            inscription.setBirthdayConjoint(LocalDate.parse( birthdayConjoint,formatter));
        }

        inscription.setBirthplaceConjoint((String) execution.getVariable("birthplaceConjoint"));
        inscription.setNationalityConjoint((String) execution.getVariable("nationalityConjoint"));
        inscription.setMobileConjoint((String) execution.getVariable("mobileConjoint"));
        inscription.setEmailConjoint((String) execution.getVariable("emailConjoint"));
        inscription.setCniNumberConjoint((String) execution.getVariable("cniNumberConjoint"));

        String cniDeliveryConjoint = (String) execution.getVariable("cniDeliveryConjoint");
        if(!cniDeliveryConjoint.isEmpty()){
            inscription.setCniDeliveryConjoint(LocalDate.parse( cniDeliveryConjoint,formatter));
        }
        inscription.setCniPlaceConjoint((String) execution.getVariable("cniPlaceConjoint"));
        inscription.setDiplomaConjoint((String) execution.getVariable("diplomaConjoint"));

        Object value = execution.getVariable("yearGraduationConjoint");
        if (value instanceof Integer) {
            inscription.setYearGraduationConjoint((Integer) value);
        }

        inscription.setSchoolConjoint((String) execution.getVariable("schoolConjoint"));
        inscription.setFormationConjoint((String) execution.getVariable("formationConjoint"));
        inscriptionRepository.save(inscription);

        log.info("Inscription Conjoint saved successfully");

        execution.setVariable("inscription", inscription);
    }

}
