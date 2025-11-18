package com.ccabank.paperless.service.impl;

import com.ccabank.paperless.dto.memo.DocumentStructure;
import com.ccabank.paperless.dto.memo.DocumentTypeDto;
import com.ccabank.paperless.entity.DocumentType;
import com.ccabank.paperless.exception.NotFoundException;
import com.ccabank.paperless.repository.DocumentTypeRepository;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.DocumentTypeService;
import com.ccabank.paperless.util.camunda.Mapping;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.form.StartFormData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class DocumentTypeServiceImpl implements DocumentTypeService {
    private final DocumentTypeRepository documentTypeRepository;
    private final CamundaService camundaService;


    @Override
    public List<DocumentTypeDto> getDocumentTypes() {
        List<DocumentType> documentTypes = documentTypeRepository.findByVisible(true);
        List<DocumentTypeDto> documentsDto = new ArrayList<>();

        for(DocumentType type : documentTypes){
            DocumentTypeDto dto = new DocumentTypeDto();
            dto.setName(type.getStructure());
            dto.setDescription(type.getName());
            //dto.setStructure(FieldUtils.getStructure(type.getStructure()));
            documentsDto.add(dto);
        }

        return documentsDto;
    }

    @Override
    public DocumentTypeDto getDocumentType(String structure) {
        DocumentType type = documentTypeRepository.findOneByStructure(structure);
        if(type == null){
            throw new NotFoundException("Structure not found");
        }
        DocumentTypeDto dto = new DocumentTypeDto();
        dto.setName(type.getStructure());
        dto.setDescription(type.getName());

        StartFormData formData = camundaService.getStartForm(type.getStructure());
        DocumentStructure documentStructure = Mapping.getStructureFromFormData(formData);

        dto.setStructure(documentStructure);
        return dto;
    }

}
