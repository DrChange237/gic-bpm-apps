package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.constant.AppError;
import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.AcceptedApprovalDto;
import com.ccabank.memoservice.dto.memo.ApprovalDto;
import com.ccabank.memoservice.dto.memo.FieldDto;
import com.ccabank.memoservice.entity.*;
import com.ccabank.memoservice.mappers.ApprovalMapper;
import com.ccabank.memoservice.repository.ApprovalRepository;
import com.ccabank.memoservice.repository.FieldRepository;
import com.ccabank.memoservice.repository.ProcessUnityRepository;
import com.ccabank.memoservice.repository.RequestRepository;
import com.ccabank.memoservice.service.faces.ApprovalService;
import com.ccabank.memoservice.service.faces.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;

@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class ApprovalServiceImpl implements ApprovalService {

    private static final Logger logger = LoggerFactory.getLogger(ApprovalServiceImpl.class);

    @Autowired
    private ApprovalMapper approvalMapper;

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private EmailService  emailService;

    @Autowired
    private ProcessUnityRepository processUnityRepository;

    @Override
    public Approval getNextPendingApproval(Request request){

        List<Approval> approvals = approvalRepository.findByRequestAndStatus(request, ApprovalStatus.PENDING).stream().sorted(Comparator.comparing(Approval::getPosition))
                .collect(Collectors.toList());

        Optional<Approval> nextApproval = approvals.stream().findFirst();

        if(nextApproval.isEmpty()){
            //throw new Exception("Pas d'approbation disponible pour cette requete");
            return null;
        }

        return nextApproval.get();

    }

    @Override
    public Approval getCurrentApproval(Request request){

        List<Approval> approvals = approvalRepository.findByRequestAndStatus(request, ApprovalStatus.WAITING).stream().sorted(Comparator.comparing(Approval::getPosition))
                .collect(Collectors.toList());

        Optional<Approval> currentApproval = approvals.stream().findFirst();
        if(currentApproval.isEmpty()){
            //throw new Exception("Pas d'approbation disponible pour cette requete");
            return null;
        }
        return currentApproval.get();
    }


    @Override
    public AppServiceResult<ApprovalDto> approve(AcceptedApprovalDto acceptedApprovalDto) {
        try {
            logger.info(MEMO_SERVICE + "approve : methode invocation");
            Approval approval = approvalRepository.getOne(acceptedApprovalDto.getIdApproval());

            if(!approval.getStatus().equals(ApprovalStatus.WAITING)){
                throw new Exception("Cette requete ne peut pas etre approuvée");
            }

            approval.setApprovalDate(LocalDateTime.now());
            approval.setStatus(ApprovalStatus.ACCEPTED);
            approval = approvalRepository.save(approval);

            for(FieldDto fieldDto : acceptedApprovalDto.getFields()){
                Field field = new Field();
                field.setKey(fieldDto.getKey());
                field.setValue(fieldDto.getValue());
                field.setApproval(approval);
                fieldRepository.save(field);
            }

            Request request = approval.getRequest();

            request.setApprobationLevel(request.getApprobationLevel() + 1);

            request = requestRepository.save(request);

            emailService.sendConfirmApproval(request, approval);


            Approval nextApproval = this.getNextPendingApproval(approval.getRequest());

            if(nextApproval == null){
                request.setStatus(RequestStatus.ACCEPTED);
                requestRepository.save(request);
            }

            nextApproval.setStatus(ApprovalStatus.WAITING);
            if(nextApproval.getType() == ApprovalType.STATIC){
                if(nextApproval.getProcessUnity() != null){
                    ProcessUnity unity = nextApproval.getProcessUnity();
                    String staffList = unity.getStaffList();
                    List<String> list = Arrays.asList(staffList.split(";"));
                    Random random = new Random();
                    int randomIndex = random.nextInt(list.size());
                    String staff = list.get(randomIndex);
                    nextApproval.setStaff(staff);
                }
            }

            nextApproval = approvalRepository.save(nextApproval);

            emailService.sendAskApproval(request,nextApproval);


            ApprovalDto dto = approvalMapper.toDto(approval);
            return new AppServiceResult<ApprovalDto>(true, 0, "Succeed!", dto);


        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<ApprovalDto>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public AppServiceResult<ApprovalDto> rejected(AcceptedApprovalDto acceptedApprovalDto) {
        try {
            logger.info(MEMO_SERVICE + "newRequest : methode invocation");
            Approval approval = approvalRepository.getOne(acceptedApprovalDto.getIdApproval());

            if(!approval.getStatus().equals(ApprovalStatus.WAITING)){
                throw new Exception("Cette requete ne peut pas etre approuvée");
            }

            approval.setApprovalDate(LocalDateTime.now());
            approval.setStatus(ApprovalStatus.REJECTED);
            approval.setComments(acceptedApprovalDto.getComments());
            approval = approvalRepository.save(approval);

            for(FieldDto fieldDto : acceptedApprovalDto.getFields()){
                Field field = new Field();
                field.setKey(fieldDto.getKey());
                field.setValue(fieldDto.getValue());
                field.setApproval(approval);
                fieldRepository.save(field);
            }


            Request request = approval.getRequest();
            request.setStatus(RequestStatus.REJECTED);
            requestRepository.save(request);

            emailService.sendRejectedApproval(request, approval);



            ApprovalDto dto = approvalMapper.toDto(approval);
            return new AppServiceResult<ApprovalDto>(true, 0, "Succeed!", dto);


        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<ApprovalDto>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }


    @Override
    public AppServiceResult<List<ApprovalDto>> getApprovalByStaff(String staff, String status) {
        try {
            logger.info(MEMO_SERVICE + "newRequest : methode invocation");
            List<Approval> approvals = approvalRepository.findByStaffAndStatus(staff, ApprovalStatus.valueOf(status));
            return getConvertedResult(approvals, "getApprovalByStaff ");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<List<ApprovalDto>>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }



    private AppServiceResult<List<ApprovalDto>> getConvertedResult(List<Approval> approvals, String functionName) {
        if (approvals == null) {
            logger.warn(MEMO_SERVICE, functionName,
                    "Feedback not exist!, Cannot further process!");
            return new AppServiceResult<List<ApprovalDto>>(false, AppError.Validattion.errorCode(),
                    "Feedback not exist!", null);
        }
        List<ApprovalDto> result =  new ArrayList<ApprovalDto>();
        if (approvals.size() > 0) {
            for (Approval approval : approvals) {
                result.add(approvalMapper.toDto(approval));
            }
        }
        return new AppServiceResult<List<ApprovalDto>>(true, 0, "Succeed!", result);
    }
}
