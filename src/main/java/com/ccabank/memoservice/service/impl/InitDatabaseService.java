package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.entity.DocumentType;
import com.ccabank.memoservice.repository.DocumentTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InitDatabaseService implements CommandLineRunner {

    private final DocumentTypeRepository documentTypeRepository;

    public InitDatabaseService(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        List<DocumentType> documentTypes = Stream.of(
                new DocumentType("Demande de congé annuel","vacation"),
                new DocumentType("Ordre de mission","mission"),
                new DocumentType("Fiche de reprise de service","resumption"),
                new DocumentType("Demande d'autorisation d'absence","absence"),
                new DocumentType("Memo","memo"),
                new DocumentType("Demande de Travail","workform"),
                new DocumentType("Demande d'Achat","purchase")

        ).filter(documentType -> !documentTypeRepository.existsByStructure(documentType.getStructure())).collect(Collectors.toList());

        documentTypeRepository.saveAll(documentTypes);

    }
}
