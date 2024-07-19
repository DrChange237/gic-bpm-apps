package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.constant.AppError;
import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.ApprovalDto;
import com.ccabank.memoservice.dto.memo.DocumentStructure;
import com.ccabank.memoservice.dto.memo.DocumentTypeDto;
import com.ccabank.memoservice.entity.ApprovalType;
import com.ccabank.memoservice.entity.DocumentType;
import com.ccabank.memoservice.repository.DocumentTypeRepository;
import com.ccabank.memoservice.service.faces.DocumentTypeService;
import com.ccabank.memoservice.util.field.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;


@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class DocumentTypeServiceImpl implements DocumentTypeService {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Override
    public AppServiceResult<List<DocumentTypeDto>> getDocumentTypes() {
        try {
            List<DocumentType> documentTypes = documentTypeRepository.findAll();
            List<DocumentTypeDto> documentsDto = new ArrayList<>();

            for(DocumentType type : documentTypes){
                DocumentTypeDto dto = new DocumentTypeDto();
                dto.setName(type.getStructure());
                dto.setDescription(type.getName());
                //dto.setStructure(FieldUtils.getStructure(type.getStructure()));
                documentsDto.add(dto);
            }

            return new AppServiceResult<List<DocumentTypeDto>>(true, 0, "Succeed!", documentsDto);

        } catch (Exception e) {
            e.printStackTrace();
            return new AppServiceResult<List<DocumentTypeDto>>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<DocumentTypeDto> getDocumentType(String structure) {
        try {
            DocumentType type = documentTypeRepository.findOneByStructure(structure);

            DocumentTypeDto dto = new DocumentTypeDto();
            dto.setName(type.getStructure());
            dto.setDescription(type.getName());
            dto.setStructure(FieldUtils.getStructure(type.getStructure()));

            return new AppServiceResult<DocumentTypeDto>(true, 0, "Succeed!", dto);

        } catch (Exception e) {
            e.printStackTrace();
            return new AppServiceResult<DocumentTypeDto>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public List<ApprovalDto> getStaticApprobals(String name) {
        try {
            DocumentStructure structure = FieldUtils.getStructure(name);
            List<ApprovalDto> approvalDtos = structure.getApprovals();
            List<ApprovalDto> staticApprobals = new ArrayList<>();

            for(ApprovalDto approbalDto: approvalDtos){
                if(approbalDto.getType() == ApprovalType.STATIC){
                    staticApprobals.add(approbalDto);
                }
            }

            return  staticApprobals;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }
}
