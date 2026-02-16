package com.change.gic.modules.business.init;


import com.change.gic.modules.business.entity.ContratTerm;
import com.change.gic.modules.business.entity.ContratTermGroup;
import com.change.gic.modules.core.repository.ContratTermGroupRepository;
import com.change.gic.modules.core.repository.ContratTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ContratTermInit implements CommandLineRunner {

    private final ContratTermRepository contratTermRepository;
    private final ContratTermGroupRepository contratTermGroupRepository;

    @Override
    public void run(String... args) {

        List<ContratTermGroup> ContratTermGroups = Stream.of(
                new ContratTermGroup("prep_doc", "Préparation de documents", 1),
                new ContratTermGroup("submit_folder", "Soumission du dossier dans le système d’Entrée Express et ARRIMA", 2),
                new ContratTermGroup("submit_rp", "Application pour la résidence permanente", 3),
                new ContratTermGroup("biometry", "Obtention de l’approbation de prise de données biométrique", 4),
                new ContratTermGroup("visa", "Obtention de l’approbation de prêt pour visa", 4),
                new ContratTermGroup("integration", "Accompagnement à l’intégration ", 5),
                new ContratTermGroup("fees", "Débours", 6)

        ).filter(contratTermGroup -> !contratTermGroupRepository.existsByCode(contratTermGroup.getCode())).collect(Collectors.toList());
        contratTermGroupRepository.saveAll(ContratTermGroups);

        ContratTermGroup prep_doc = contratTermGroupRepository.findByCode("prep_doc");
        List<ContratTerm> ContratTerms = Stream.of(
                new ContratTerm(prep_doc,"prep_doc_1", "Donner les conseils pour la préparation des documents d’immigration", 1),
                new ContratTerm(prep_doc,"prep_doc_2", "Faire les équivalences de diplôme étrangère", 2),
                new ContratTerm(prep_doc,"prep_doc_3", "Faire le suivi auprès de organisme d’équivalences de diplôme étrangère;", 3),
                new ContratTerm(prep_doc,"prep_doc_4", "Mettre à la disposition du candidat le nécessaire pour la préparation aux tests de langue et entreprendre les démarches pour son inscription.", 4)

        ).filter(contratTerm -> !contratTermRepository.existsByCode(contratTerm.getCode())).collect(Collectors.toList());
        contratTermRepository.saveAll(ContratTerms);

        ContratTermGroup submit_folder = contratTermGroupRepository.findByCode("submit_folder");
        ContratTerms = Stream.of(
                new ContratTerm(submit_folder,"submit_folder_1", "Rassembler tous les documents nécessaires pour l’ouverture du compte ", 1),
                new ContratTerm(submit_folder,"submit_folder_2", "Créer le compte dans le système d’Entrée express", 2),
                new ContratTerm(submit_folder,"submit_folder_3", "Compléter les informations", 3),
                new ContratTerm(submit_folder,"submit_folder_4", "Faire les mises à jour des informations", 4),
                new ContratTerm(submit_folder,"submit_folder_5", "Faire le suivi auprès de l’IRCC ;", 5),
                new ContratTerm(submit_folder,"submit_folder_6", "Appliquer pour les programmes de province [au besoin]", 6)

        ).filter(contratTerm -> !contratTermRepository.existsByCode(contratTerm.getCode())).collect(Collectors.toList());
        contratTermRepository.saveAll(ContratTerms);

        ContratTermGroup submit_rp = contratTermGroupRepository.findByCode("submit_rp");
        ContratTerms = Stream.of(
                new ContratTerm(submit_rp,"submit_rp_1", "Rassembler et vérifier tous les documents nécessaires;", 1),
                new ContratTerm(submit_rp,"submit_rp_2", "Soumettre la demande portail de résidence permanente", 2),
                new ContratTerm(submit_rp,"submit_rp_3", "Effectuer les paiements des frais de procédure", 3),
                new ContratTerm(submit_rp,"submit_rp_4", "Créer un compte IRCC et lier la demande", 4),
                new ContratTerm(submit_rp,"submit_rp_5", "Faire les mises à jour et le suivi de la demande", 5)

        ).filter(contratTerm -> !contratTermRepository.existsByCode(contratTerm.getCode())).collect(Collectors.toList());
        contratTermRepository.saveAll(ContratTerms);

        ContratTermGroup biometry = contratTermGroupRepository.findByCode("biometry");
        ContratTerms = Stream.of(
                new ContratTerm(biometry,"biometry_1", "Rassembler et vérifier tous les documents nécessaires;", 1),
                new ContratTerm(biometry,"biometry_2", "Prise de rendez-vous et suivi;", 2)

        ).filter(contratTerm -> !contratTermRepository.existsByCode(contratTerm.getCode())).collect(Collectors.toList());
        contratTermRepository.saveAll(ContratTerms);

        ContratTermGroup visa = contratTermGroupRepository.findByCode("visa");
        ContratTerms = Stream.of(
                new ContratTerm(visa,"visa_1", "Acheminer les documents nécessaires pour l’obtention du visa", 1)

        ).filter(contratTerm -> !contratTermRepository.existsByCode(contratTerm.getCode())).collect(Collectors.toList());
        contratTermRepository.saveAll(ContratTerms);

        ContratTermGroup integration = contratTermGroupRepository.findByCode("integration");
        ContratTerms = Stream.of(
                new ContratTerm(integration,"integration_1", "Recherche et réservation de logement", 1),
                new ContratTerm(integration,"integration_2", "Aide à obtention des documents officiels", 2)

        ).filter(contratTerm -> !contratTermRepository.existsByCode(contratTerm.getCode())).collect(Collectors.toList());
        contratTermRepository.saveAll(ContratTerms);

        ContratTermGroup fees = contratTermGroupRepository.findByCode("fees");
        ContratTerms = Stream.of(
                new ContratTerm(fees,"fees_1", "Frais demande de résidence Permanente", 1),
                new ContratTerm(fees,"fees_2", "Frais de visa", 2),
                new ContratTerm(fees,"fees_3", "Frais de biométrie", 3),
                new ContratTerm(fees,"fees_4", "Frais administratifs", 4),
                new ContratTerm(fees,"fees_5", "Honoraire", 5),
                new ContratTerm(fees,"fees_6", "Frais de test de langue", 6),
                new ContratTerm(fees,"fees_7", "Frais de preuve de fonds", 7),
                new ContratTerm(fees,"fees_8", "Examens médicaux", 8),
                new ContratTerm(fees,"fees_9", "Frais de réservation de logement", 9)

        ).filter(contratTerm -> !contratTermRepository.existsByCode(contratTerm.getCode())).collect(Collectors.toList());
        contratTermRepository.saveAll(ContratTerms);

    }
}
