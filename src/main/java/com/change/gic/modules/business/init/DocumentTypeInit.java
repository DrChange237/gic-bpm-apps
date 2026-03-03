package com.change.gic.modules.business.init;

import com.change.gic.modules.business.entity.DocumentType;
import com.change.gic.modules.business.entity.Program;
import com.change.gic.modules.business.repository.DocumentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DocumentTypeInit implements CommandLineRunner {

    private final DocumentTypeRepository documentTypeRepository;

    @Override
    public void run(String... args) throws Exception {

        documentTypeRepository.deleteAll();

        List<DocumentType> documentTypes = Stream.of(

                new DocumentType("Passport - Principal", "passport_principal", false),
                new DocumentType("Passport - Conjoint", "passport_conjoint", false),
                new DocumentType("Passport - Enfants", "passport_children", true),
                new DocumentType("Equivalence de diplômes (EDE) - Principal", "equivalence_principal", false),
                new DocumentType("Equivalence de diplômes (EDE) - Conjoint", "equivalence_conjoint", false),
                new DocumentType("Diplômes et relevés de notes postsecondaires - Principal", "diploma_principal", false),
                new DocumentType("Diplômes et relevés de notes postsecondaires - Conjoint", "diploma_conjoint", false),
                new DocumentType("Résultats du test de langue - Principal", "testlang_principal", false),
                new DocumentType("Résultats du test de langue - Conjoint", "testlang_conjoint", false),
                new DocumentType("Attestation(s) de travail - Principal", "attestation_principal", false),
                new DocumentType("Attestation(s) de travail - Conjoint", "attestation_conjoint", false),
                new DocumentType("Certificat de police - Principal", "certificat_police_principal", false),
                new DocumentType("Certificat de police - Conjoint", "certificat_police_conjoint", false),
                new DocumentType("Photographies 5 x 7 cm  - Principal", "photography_principal", false),
                new DocumentType("Photographies 5 x 7 cm  - Conjoint", "photography_conjoint", false),
                new DocumentType("Photographies 5 x 7 cm  - Enfants", "photography_children", true),
                new DocumentType("Acte de naissance  - Principal", "acte_naissance_principal", false),
                new DocumentType("Acte de naissance  - Conjoint", "acte_naissance_conjoint", false),
                new DocumentType("Acte de naissance  - Enfants", "acte_naissance_children", true),
                new DocumentType("Visite Médical  - Principal", "medical_visit_principal", false),
                new DocumentType("Visite Médical  - Conjoint", "medical_visit_conjoint", false),
                new DocumentType("Visite Médical  - Enfants", "medical_visit_children", true),
                new DocumentType("Acte de Mariage", "acte_mariage", false),
                new DocumentType("Attestation d’union de fait", "union_fait", false),
                new DocumentType("Autorisation parentale notariée", "autorisation_parental", true)
                

                ).filter(documentType -> !documentTypeRepository.existsByTag(documentType.getTag())).collect(Collectors.toList());
        documentTypeRepository.saveAll(documentTypes);

    }
}
