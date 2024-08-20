package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.entity.DocumentType;
import com.ccabank.memoservice.entity.ProcessUnity;
import com.ccabank.memoservice.repository.DocumentTypeRepository;
import com.ccabank.memoservice.repository.ProcessUnityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InitDatabaseService implements CommandLineRunner {

    private final DocumentTypeRepository documentTypeRepository;

    private final ProcessUnityRepository processUnityRepository;

    public InitDatabaseService(DocumentTypeRepository documentTypeRepository, ProcessUnityRepository processUnityRepository) {
        this.documentTypeRepository = documentTypeRepository;
        this.processUnityRepository = processUnityRepository;
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

        List<ProcessUnity> processUnities = Stream.of(
                new ProcessUnity("RH","Capital Humain", ""),
                new ProcessUnity("DG","Direction Générale", ""),
                new ProcessUnity("DGA","Direction Générale Adjointe", ""),
                new ProcessUnity("DAF","Direction Administrative et Financière", ""),
                new ProcessUnity("COMPTA","Comptabilité", ""),
                new ProcessUnity("MG","Moyens Généraux", ""),
                new ProcessUnity("ARMG","Adjoint Responsable Moyens Généraux", ""),
                new ProcessUnity("RMG","Responsable Moyens Généraux", ""),
                new ProcessUnity("CB","Controle Budgétaire", ""),
                new ProcessUnity("FISCAL","Fiscalité", ""),
                new ProcessUnity("DOP","Direction des Opérations", "")

        ).filter(processUnity -> !processUnityRepository.existsByCode(processUnity.getCode())).collect(Collectors.toList());

        processUnityRepository.saveAll(processUnities);


    }
}
