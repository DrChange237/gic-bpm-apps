package com.change.gic.modules.business.service.impl;

import com.change.gic.modules.business.entity.*;
import com.change.gic.modules.business.service.faces.PDFGenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

import com.lowagie.text.DocumentException;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class PDFGenerationServiceImpl implements PDFGenerationService {

    private final TemplateEngine templateEngine;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public byte[] generateCustomerProfilePdf(Inscription inscription) throws DocumentException, IOException {
        // Créer le contexte Thymeleaf
        Context context = new Context();
        context.setVariable("inscription", inscription);
        context.setVariable("dateFormatter", DATE_FORMATTER);
        // Générer le HTML à partir du template
        String htmlContent = templateEngine.process("customer-profile", context);

        // Convertir HTML en PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        return outputStream.toByteArray();
    }

    @Override
    public byte[] generateConsultationReport(Consultation consultation) throws DocumentException, IOException {
        // Créer le contexte Thymeleaf
        Context context = new Context();
        context.setVariable("consultation", consultation);
        context.setVariable("dateFormatter", DATE_FORMATTER);
        // Générer le HTML à partir du template
        String htmlContent = templateEngine.process("consultation-report", context);

        // Convertir HTML en PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        return outputStream.toByteArray();
    }

    @Override
    public byte[] generateContract(Contrat contrat,
                                   List<ContratTermGroup> contratTermGroups,
                                   List<ContratTerm> contratTerms,
                                   List<ContratTerm> allDebours,
                                   List<ContratTerm> debours
    ) throws DocumentException, IOException {
        // Créer le contexte Thymeleaf
        Context context = new Context();
        context.setVariable("contrat", contrat);
        context.setVariable("contratTermGroups", contratTermGroups);
        context.setVariable("contratTerms", contratTerms);
        context.setVariable("allDebours", allDebours);
        context.setVariable("debours", debours);


        context.setVariable("dateFormatter", DATE_FORMATTER);
        // Générer le HTML à partir du template
        String htmlContent = templateEngine.process("contract-signed", context);

        // Convertir HTML en PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        return outputStream.toByteArray();
    }



}
