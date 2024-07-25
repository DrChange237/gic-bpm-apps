package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.reporting.MissionForm;
import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.OrdreMission;
import com.ccabank.memoservice.entity.documenttype.Signatory;
import com.ccabank.memoservice.entity.documenttype.Staff;
import com.ccabank.memoservice.openfeign.ReportingRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.RequestRepository;
import com.ccabank.memoservice.service.faces.OrdreMissionService;
import com.ccabank.memoservice.service.faces.SaveDocumentService;
import com.ccabank.memoservice.util.field.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;
import static com.ccabank.memoservice.constant.DocumentTypeConstant.*;
import static com.ccabank.memoservice.constant.DocumentTypeConstant.DOCUMENT_TYPE_RESUMPTION;

@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class SaveDocumentServiceImpl implements SaveDocumentService {

    private static final Logger logger = LoggerFactory.getLogger(SaveDocumentServiceImpl.class);

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private OrdreMissionService ordreMissionService;

    @Autowired
    private RequestRepository requestRepository;

    @Override
    public void saveDocument(Request request){

        switch (request.getType().getStructure()){

            case DOCUMENT_TYPE_VACATION :

                break;

            case DOCUMENT_TYPE_ABSENSE:



                break;


            case DOCUMENT_TYPE_MISSION:

                OrdreMission ordreMission = this.ordreMissionService.save(request);
                request.setDocumentId(ordreMission.getId());
                this.requestRepository.save(request);

                break;

            case DOCUMENT_TYPE_RESUMPTION:



                break;


        }

    }

}
