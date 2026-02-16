package com.change.gic.modules.business.service.faces;

import com.change.gic.modules.business.dto.CustomerProfileDto;
import com.change.gic.modules.business.entity.*;
import com.lowagie.text.DocumentException;

import java.io.IOException;
import java.util.List;

public interface PDFGenerationService {

    byte[] generateCustomerProfilePdf(Inscription inscription) throws DocumentException, IOException;

    byte[] generateConsultationReport(Consultation consultation) throws DocumentException, IOException;

    byte[] generateContract(Contrat contrat,
                            List<ContratTermGroup> contratTermGroups,
                            List<ContratTerm> contratTerms,
                            List<ContratTerm> allDebours,
                            List<ContratTerm> debours
    ) throws DocumentException, IOException;
}
