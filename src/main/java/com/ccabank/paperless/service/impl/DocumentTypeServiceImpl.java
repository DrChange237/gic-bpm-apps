package com.ccabank.paperless.service.impl;

import com.ccabank.paperless.constant.AppError;
import com.ccabank.paperless.domain.AppServiceResult;
import com.ccabank.paperless.dto.memo.DocumentStructure;
import com.ccabank.paperless.dto.memo.DocumentTypeDto;
import com.ccabank.paperless.entity.DocumentType;
import com.ccabank.paperless.repository.DocumentTypeRepository;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.DocumentTypeService;
import com.ccabank.paperless.util.camunda.Mapping;
import org.camunda.bpm.engine.form.StartFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ccabank.paperless.constant.BeanIdConstant.MEMO_SERVICE;


@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class DocumentTypeServiceImpl implements DocumentTypeService {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private CamundaService camundaService;


    @Override
    public AppServiceResult<List<DocumentTypeDto>> getDocumentTypes() {
        try {
            List<DocumentType> documentTypes = documentTypeRepository.findByVisible(true);
            List<DocumentTypeDto> documentsDto = new ArrayList<>();

            for(DocumentType type : documentTypes){
                DocumentTypeDto dto = new DocumentTypeDto();
                dto.setName(type.getStructure());
                dto.setDescription(type.getName());
                //dto.setStructure(FieldUtils.getStructure(type.getStructure()));
                documentsDto.add(dto);
            }

            return new AppServiceResult<>(true, 0, "Succeed!", documentsDto);

        } catch (Exception e) {
            e.printStackTrace();
            return new AppServiceResult<>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<DocumentTypeDto> getDocumentType(String structure) {
        try {
            DocumentType type = documentTypeRepository.findOneByStructure(structure);
            if(type == null){
                throw new Exception("Structure not found");
            }
            DocumentTypeDto dto = new DocumentTypeDto();
            dto.setName(type.getStructure());
            dto.setDescription(type.getName());

            StartFormData formData = camundaService.getStartForm(type.getStructure());
            DocumentStructure documentStructure = Mapping.getStructureFromFormData(formData);

            dto.setStructure(documentStructure);
            return new AppServiceResult<>(true, 0, "Succeed!", dto);

        } catch (Exception e) {
            e.printStackTrace();
            return new AppServiceResult<>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

}
