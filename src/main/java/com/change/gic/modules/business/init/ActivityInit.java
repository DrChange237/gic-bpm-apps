package com.change.gic.modules.business.init;


import com.change.gic.modules.core.entity.ActivityUserTask;
import com.change.gic.modules.core.repository.ActivityUserTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ActivityInit implements CommandLineRunner {

    private final ActivityUserTaskRepository activityUserTaskRepository;

    @Override
    public void run(String... args) throws Exception {

        activityUserTaskRepository.deleteAll();

        HashMap<String, String> consultation = new HashMap<>();
        consultation.put("firstName", "Noms du client");
        consultation.put("lastName", "Prénoms du client");
        consultation.put("diploma", "Dernier Diplôme");
        consultation.put("yearGraduation", "Année d'obtention");
        consultation.put("experience", "Expérience");
        consultation.put("mobile", "Mobile");
        consultation.put("email", "Email");

        HashMap<String, String> setup_contract = new HashMap<>();
        setup_contract.put("firstName", "Noms du client");
        setup_contract.put("lastName", "Prénoms du client");
        setup_contract.put("diploma", "Dernier Diplôme");
        setup_contract.put("yearGraduation", "Année d'obtention");
        setup_contract.put("experience", "Expérience");
        setup_contract.put("mobile", "Mobile");
        setup_contract.put("email", "Email");

        HashMap<String, String> verify_result_test = new HashMap<>();
        verify_result_test.put("firstName", "Noms du client");
        verify_result_test.put("lastName", "Prénoms du client");
        verify_result_test.put("diploma", "Dernier Diplôme");
        verify_result_test.put("yearGraduation", "Année d'obtention");
        verify_result_test.put("experience", "Expérience");
        verify_result_test.put("note_ee", "Note Expréssion Ecrite");
        verify_result_test.put("note_eo", "Note Expréssion Orale");
        verify_result_test.put("note_ce", "Note Compréhension Ecrite");
        verify_result_test.put("note_co", "Note Compréhension Orale");
        verify_result_test.put("mobile", "Mobile");
        verify_result_test.put("email", "Email");

        HashMap<String, String> activate_collaborator = new HashMap<>();
        activate_collaborator.put("role", "Rôle");
        activate_collaborator.put("agency", "Agence");
        activate_collaborator.put("username", "Username");
        activate_collaborator.put("email", "Email");

        HashMap<String, String> associate_document = setup_contract;
        associate_document.put("document_name", "Document à Ajouter");






        List<ActivityUserTask> activityUserTasks = Stream.of(

                new ActivityUserTask("consultation", consultation),
                new ActivityUserTask("setup_contract", setup_contract),
                new ActivityUserTask("contract_customer_confirm", setup_contract),
                new ActivityUserTask("validation_contract", setup_contract),
                new ActivityUserTask("type_cashin", setup_contract),
                new ActivityUserTask("validation_cashin", setup_contract),
                new ActivityUserTask("upload_diploma", setup_contract),
                new ActivityUserTask("create_equivalence_profil", setup_contract),
                new ActivityUserTask("certify_diploma", setup_contract),
                new ActivityUserTask("authentification_diploma", setup_contract),
                new ActivityUserTask("missing_element_equivalence", setup_contract),
                new ActivityUserTask("equivalence_confirm_notif", setup_contract),
                new ActivityUserTask("contact_test_student", setup_contract),
                new ActivityUserTask("register_test_exam", setup_contract),
                new ActivityUserTask("verify_result_test", verify_result_test),
                new ActivityUserTask("result_test_lang", setup_contract),
                new ActivityUserTask("confirm_level_test", setup_contract),
                new ActivityUserTask("create_profil_express", verify_result_test),
                new ActivityUserTask("confirm_ita", setup_contract),
                new ActivityUserTask("create_arrima_profil", verify_result_test),
                new ActivityUserTask("accept_invite_csq", setup_contract),
                new ActivityUserTask("submit_csq", setup_contract),
                new ActivityUserTask("confirm_recept_csq", setup_contract),
                new ActivityUserTask("confirm_invite_csq", setup_contract),

                new ActivityUserTask("medical_visit", setup_contract),
                new ActivityUserTask("upload_doc_rp", setup_contract),
                new ActivityUserTask("payment_fees_rp", setup_contract),
                new ActivityUserTask("biometry_rdv", setup_contract),
                new ActivityUserTask("submit_rp", setup_contract),
                new ActivityUserTask("confirm_medical_visit", setup_contract),
                new ActivityUserTask("confirm_biometry", setup_contract),
                new ActivityUserTask("confirm_rp", setup_contract),
                new ActivityUserTask("back_diploma", setup_contract),
                new ActivityUserTask("activate_collaborator", activate_collaborator),
                new ActivityUserTask("associate_document", associate_document)

        ).filter(activityUserTask -> !activityUserTaskRepository.existsByTaskDefinitionKey(activityUserTask.getTaskDefinitionKey())).collect(Collectors.toList());
        activityUserTaskRepository.saveAll(activityUserTasks);

    }

}
