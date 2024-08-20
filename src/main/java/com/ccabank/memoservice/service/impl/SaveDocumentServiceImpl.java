package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.reporting.PurchaseForm;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.*;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.MemoRepository;
import com.ccabank.memoservice.repository.RequestRepository;
import com.ccabank.memoservice.service.faces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private VacationService vacationService;

    @Autowired
    private AbsenceService absenceService;

    @Autowired
    private ResumptionService resumptionService;

    @Autowired
    private MemoService memoService;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private WorkformService workformService;

    @Autowired
    private PurchaseService purchaseService;


    @Override
    public void saveDocument(Request request){

        switch (request.getType().getStructure()){

            case DOCUMENT_TYPE_VACATION:

                Vacation vacation = this.vacationService.save(request);
                request.setDocumentId(vacation.getId());
                this.requestRepository.save(request);

                break;

            case DOCUMENT_TYPE_ABSENSE:

                Absence absence = this.absenceService.save(request);
                request.setDocumentId(absence.getId());
                this.requestRepository.save(request);

                break;


            case DOCUMENT_TYPE_MISSION:

                OrdreMission ordreMission = this.ordreMissionService.save(request);
                request.setDocumentId(ordreMission.getId());
                this.requestRepository.save(request);

                break;

            case DOCUMENT_TYPE_RESUMPTION:

                Resumption resumption = this.resumptionService.save(request);
                request.setDocumentId(resumption.getId());
                this.requestRepository.save(request);

                break;

            case DOCUMENT_TYPE_MEMO:

                Memo memo = this.memoService.save(request);
                request.setDocumentId(memo.getId());
                this.requestRepository.save(request);

                break;

            case DOCUMENT_TYPE_WORKFORM:

                WorkForm workForm = this.workformService.save(request);
                request.setDocumentId(workForm.getId());
                this.requestRepository.save(request);
                break;

            case DOCUMENT_TYPE_PURCHASE:

                Purchase purchase = this.purchaseService.save(request);
                request.setDocumentId(purchase.getId());
                this.requestRepository.save(request);
                break;
        }

    }

}
