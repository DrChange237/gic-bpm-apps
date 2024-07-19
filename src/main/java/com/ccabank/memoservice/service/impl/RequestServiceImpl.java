package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.constant.AppError;
import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.ApprovalDto;
import com.ccabank.memoservice.dto.memo.FieldDto;
import com.ccabank.memoservice.dto.memo.RequestDto;
import com.ccabank.memoservice.entity.*;
import com.ccabank.memoservice.mappers.RequestMapper;
import com.ccabank.memoservice.repository.*;
import com.ccabank.memoservice.service.faces.ApprovalService;
import com.ccabank.memoservice.service.faces.EmailService;
import com.ccabank.memoservice.service.faces.MapToReportService;
import com.ccabank.memoservice.service.faces.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;

@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class RequestServiceImpl implements RequestService {

    private static final Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private ProcessUnityRepository processUnityRepository;

    @Autowired
    private MapToReportService mapToReportService;

    @Autowired
    private EmailService emailService;

    @Override
    public AppServiceResult<Request> newRequest(RequestDto requestDto) {
        try {
            logger.info(MEMO_SERVICE + "newRequest : methode invocation");

            Request request = new Request();
            request.setCreatedAt(LocalDateTime.now());
            request.setStaff(requestDto.getStaff());

            DocumentType type = documentTypeRepository.findOneByStructure(requestDto.getDocumentType());
            request.setType(type);
            request.setStatus(RequestStatus.DRAFT);
            request.setApprobationLevel(0);

            request = requestRepository.save(request);

            //DocumentStructure stucture = FieldUtils.getStructure(type.getStructure());

            for(FieldDto fieldDto : requestDto.getFields()){
                Field field = new Field();
                field.setKey(fieldDto.getKey());
                field.setValue(fieldDto.getValue());
                field.setRequest(request);
                fieldRepository.save(field);
            }

            for(ApprovalDto approvalDto : requestDto.getApprovals()){
                Approval approval = new Approval();
                approval.setPosition(approvalDto.getPosition());
                approval.setStatus(ApprovalStatus.PENDING);
                approval.setStaff(approvalDto.getStaff());
                approval.setRole(approvalDto.getRole());
                ProcessUnity processUnity = processUnityRepository.findOneByCode(approvalDto.getUnity());
                approval.setProcessUnity(processUnity);
                approval.setRequest(request);
                approvalRepository.save(approval);
            }

            return new AppServiceResult<Request>(true, 0, "Succeed!", request );

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " newRequest : Exception {}", e.getMessage());
            return new AppServiceResult<Request>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public InputStream downloadRequest(Long id) {
        try {
            logger.info(MEMO_SERVICE + "newRequest : methode invocation");

            Request request = requestRepository.getOne(id);

            if(request == null){
                throw new Exception("Aucune requete retrouvée");
            }

            if(!request.getStatus().equals(RequestStatus.ACCEPTED)){
                throw new Exception("Cette requete n'est pas validée");
            }

            InputStream input = mapToReportService.reportRequest(request);

            return input;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " validateRequest : Exception {}", e.getMessage());
            return null;

        }
    }

    @Override
    public AppServiceResult<Request> validateRequest(Long id) {
        try {
            logger.info(MEMO_SERVICE + "newRequest : methode invocation");

            Request request = requestRepository.getOne(id);

            if(request == null){
                throw new Exception("Aucune requete retrouvée");
            }

            if(!request.getStatus().equals(RequestStatus.DRAFT)){
                throw new Exception("Cette requete à déjà été validée");
            }

            Approval approval = approvalService.getNextPendingApproval(request);
            approval.setStatus(ApprovalStatus.WAITING);
            approval = approvalRepository.save(approval);
            request.setStatus(RequestStatus.PENDING);
            request = requestRepository.save(request);

            emailService.sendAskApproval(request, approval);

            //DocumentStructure stucture = FieldUtils.getStructure(type.getStructure());

            return new AppServiceResult<Request>(true, 0, "Succeed!", request );

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " validateRequest : Exception {}", e.getMessage());
            return new AppServiceResult<Request>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public AppServiceResult<List<RequestDto>> getRequestByStaff(String staff, String status) {
        try {
            logger.info(MEMO_SERVICE + "newRequest : methode invocation");

            List<Request> requests = requestRepository.findByStaffAndStatus(staff, RequestStatus.valueOf(status));

            return getConvertedResult(requests, "getRequestByStaff ");


        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<List<RequestDto>>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    private AppServiceResult<List<RequestDto>> getConvertedResult(List<Request> requests, String functionName) {
        if (requests == null) {
            logger.warn(MEMO_SERVICE, functionName,
                    "Feedback not exist!, Cannot further process!");
            return new AppServiceResult<List<RequestDto>>(false, AppError.Validattion.errorCode(),
                    "Feedback not exist!", null);
        }
        List<RequestDto> result =  new ArrayList<RequestDto>();
        if (requests.size() > 0) {
            for (Request request : requests) {
                result.add(requestMapper.toDto(request));
            }
        }
        return new AppServiceResult<List<RequestDto>>(true, 0, "Succeed!", result);
    }



}
