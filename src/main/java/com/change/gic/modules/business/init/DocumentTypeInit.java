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

        List<DocumentType> documentTypes = Stream.of(

                new DocumentType("Diplôme", "diploma"),
                new DocumentType("Equivalence de Diplôme", "equivalence"),
                new DocumentType("Resultat Test de Langue", "test_result"),
                new DocumentType("Passport", "passport"),
                new DocumentType("Casier Judiciaire", "casier_judiciaire"),
                new DocumentType("Acte de Naissance", "acte_naissance"),
                new DocumentType("Certificat de Selection du Quebec", "csq")

        ).filter(documentType -> !documentTypeRepository.existsByTag(documentType.getTag())).collect(Collectors.toList());
        documentTypeRepository.saveAll(documentTypes);

    }
}
