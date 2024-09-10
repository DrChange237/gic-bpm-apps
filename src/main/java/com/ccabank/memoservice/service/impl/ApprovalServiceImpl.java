package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.constant.AppError;
import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.*;
import com.ccabank.memoservice.dto.workflow.Transition;
import com.ccabank.memoservice.dto.workflow.WorkflowManager;
import com.ccabank.memoservice.entity.*;
import com.ccabank.memoservice.mappers.ApprovalListMapper;
import com.ccabank.memoservice.mappers.ApprovalMapper;
import com.ccabank.memoservice.mappers.RequestInfoMapper;
import com.ccabank.memoservice.mappers.RequestMapper;
import com.ccabank.memoservice.openfeign.AuthRestClient;
import com.ccabank.memoservice.repository.ApprovalRepository;
import com.ccabank.memoservice.repository.FieldRepository;
import com.ccabank.memoservice.repository.ProcessUnityRepository;
import com.ccabank.memoservice.repository.RequestRepository;
import com.ccabank.memoservice.service.faces.ApprovalService;
import com.ccabank.memoservice.service.faces.EmailService;
import com.ccabank.memoservice.service.faces.SaveDocumentService;
import com.ccabank.memoservice.util.field.ApprobalUtils;
import com.ccabank.memoservice.util.field.FieldUtils;
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
    private RequestMapper requestMapper;

    @Autowired
    private RequestInfoMapper requestInfoMapper;

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private EmailService  emailService;

    @Autowired
    private SaveDocumentService saveDocumentService;

    @Autowired
    private ApprovalListMapper approvalListMapper;

    @Autowired
    private AuthRestClient authRestClient;


    @Autowired
    private ProcessUnityRepository processUnityRepository;

    @Override
    public Approval getApprovalWithPosition(Request request, int position){

        List<Approval> approvals = approvalRepository.findByRequestAndPosition(request,  position).stream().sorted(Comparator.comparing(Approval::getPosition))
                .collect(Collectors.toList());

        Optional<Approval> nextApproval = approvals.stream().findFirst();

        if(nextApproval.isEmpty()){
            //throw new Exception("Pas d'approbation disponible pour cette requete");
            return null;
        }

        return nextApproval.get();
    }

    @Override
    public Approval getNextPendingApproval(Approval approval){

        Request request = approval.getRequest();

        ApprovalDto structure = ApprobalUtils.getApprobalStructure(request.getType().getStructure(), approval.getPosition());

        if (structure.getNext() != null){

             for (Transition transition : structure.getNext()){

                 if(WorkflowManager.evaluateCondition(transition.getCondition(), approval)){

                     int to = transition.getTo();

                     return this.getApprovalWithPosition(request, to);

                 }
             }
        }

        return this.getApprovalWithPosition(request, approval.getPosition() + 1);
    }

    @Override
    public Approval getNextApproval(Request request){

        List<Approval> approvals = approvalRepository.findByRequest(request).stream().sorted(Comparator.comparing(Approval::getPosition))
                .collect(Collectors.toList());

        Optional<Approval> nextApproval = approvals.stream().findFirst();

        if(nextApproval.isEmpty()){
            //throw new Exception("Pas d'approbation disponible pour cette requete");
            return null;
        }
        return nextApproval.get();
    }

    public Approval getPrevAccepted(Request request){

        List<Approval> approvals = approvalRepository.findByRequestAndStatus(request, ApprovalStatus.ACCEPTED).stream().sorted(Comparator.comparing(Approval::getPosition).reversed())
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

        List<Approval> approvals = approvalRepository.findByRequestAndStatus(request, ApprovalStatus.WAITING).stream().sorted(Comparator.comparing(Approval::getPosition).reversed())
                .collect(Collectors.toList());

        Optional<Approval> currentApproval = approvals.stream().findFirst();
        if(currentApproval.isEmpty()){
            //throw new Exception("Pas d'approbation disponible pour cette requete");
            return null;
        }
        return currentApproval.get();
    }

    @Override
    public AppServiceResult<?> decision(AcceptedApprovalDto acceptedApprovalDto)  {

          if(acceptedApprovalDto.isDecision()){
              return this.approve(acceptedApprovalDto);
          }else{
              return this.rejected(acceptedApprovalDto);

          }
    }

    @Override
    public AppServiceResult<?> reassign(ReassignDto reassignDto)  {
        try {
            Approval approval = this.approvalRepository.getOne(reassignDto.getIdApproval());
            if (!(approval.getStatus() == ApprovalStatus.WAITING || approval.getStatus() == ApprovalStatus.PENDING)){
                    throw new Exception("Cette approbation ne peut etre réassigné");
            }

            approval.setStaff(reassignDto.getStaff());

            Request request = approval.getRequest();
            request.setLastModification(LocalDateTime.now());
            requestRepository.save(request);


            this.emailService.sendAskApproval(approval.getRequest(), approval);

            ApprovalDto approvalDto = this.approvalMapper.toDto(approval);

            return new AppServiceResult<>(true, 0, "Succeed!", approvalDto);

        } catch (Exception e) {
            return new AppServiceResult<>(false, AppError.Unknown.errorCode(), e.getMessage(), null);
        }
    }


    @Override
    public AppServiceResult<?> approve(AcceptedApprovalDto acceptedApprovalDto) {
        try {
            logger.info(MEMO_SERVICE + "approve : methode invocation");
            Approval approval = approvalRepository.getOne(acceptedApprovalDto.getIdApproval());


            if(!approval.getStatus().equals(ApprovalStatus.WAITING)){
                throw new Exception("Cette requete ne peut pas etre approuvée");
            }


            String type = approval.getRequest().getType().getStructure();
            List<FieldDto> stuctureFields = FieldUtils.getFieldsOfTypeAndPosition(type, approval.getPosition());

            List<FieldDto> incommingFields = acceptedApprovalDto.getFields();

            if(stuctureFields != null){
                for(FieldDto fieldDto : stuctureFields){
                    if(fieldDto.isRequired()){
                        if(incommingFields == null){
                            throw new Exception("champs : " + fieldDto.getKey() + " requis");
                        }
                        Optional<FieldDto> fTmp = incommingFields.stream().filter(obj -> obj.getKey().equals(fieldDto.getKey())).findFirst();
                        if(fTmp.isEmpty()){
                            throw new Exception("champs : " + fieldDto.getKey() + " requis");
                        }
                    }
                }
            }


            if(incommingFields != null){
                for(FieldDto fieldDto : incommingFields){
                    Field field = new Field();
                    field.setKey(fieldDto.getKey());
                    field.setValue(fieldDto.getValue());
                    field.setApproval(approval);
                    field.setRequest(approval.getRequest());
                    fieldRepository.save(field);
                }
            }

            approval.setApprovalDate(LocalDateTime.now());
            approval.setStatus(ApprovalStatus.ACCEPTED);
            approval.setComments(acceptedApprovalDto.getComments());
            approval = approvalRepository.save(approval);

            Request request = approval.getRequest();

            request.setApprobationLevel(approval.getPosition());

            request.setLastModification(LocalDateTime.now());

            request = requestRepository.save(request);


            Approval nextApproval = this.getNextPendingApproval(approval);

            if(nextApproval == null){
                System.out.println("Pas de Next Approval");

                request.setStatus(RequestStatus.ACCEPTED);

                System.out.println("Requete Acceptée");
                request = requestRepository.save(request);

                System.out.println("Sauvegarde du Document");

                saveDocumentService.saveDocument(request);
                emailService.sendConfirmRequest(request);
            }else{
                nextApproval.setStatus(ApprovalStatus.WAITING);
                if(nextApproval.getType() == ApprovalType.STATIC){
                    if(nextApproval.getProcessUnity() != null){
                        ProcessUnity unity = nextApproval.getProcessUnity();
                        nextApproval = approvalRepository.save(nextApproval);
                        emailService.sendAskApprovalUnity(request, nextApproval, unity);
                        String staffList = unity.getStaffList();
                        List<String> list = Arrays.asList(staffList.split(";"));
                        Random random = new Random();
                        int randomIndex = random.nextInt(list.size());
                        String staff = list.get(randomIndex);
                        nextApproval.setStaff(staff);
                    }
                }else{
                    nextApproval = approvalRepository.save(nextApproval);
                    emailService.sendAskApproval(request,nextApproval);
                }
            }

            emailService.sendConfirmApproval(request, approval);

            ApprovalDto dto = approvalMapper.toDto(approval);
            return new AppServiceResult<>(true, 0, "Succeed!", dto);


        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public AppServiceResult<?> rejected(AcceptedApprovalDto acceptedApprovalDto) {
        try {
            logger.info(MEMO_SERVICE + "newRequest : methode invocation");
            Approval approval = approvalRepository.getOne(acceptedApprovalDto.getIdApproval());

            if (acceptedApprovalDto.getComments() == null) {
                throw new Exception("Le commentaires est obligatoire en cas de refus");
            }

            if(!approval.getStatus().equals(ApprovalStatus.WAITING)){
                throw new Exception("Cette requete ne peut pas etre approuvée");
            }

            approval.setApprovalDate(LocalDateTime.now());
            //approval.setStatus(ApprovalStatus.REJECTED);
            approval.setComments(acceptedApprovalDto.getComments());
            approval = approvalRepository.save(approval);

            for(FieldDto fieldDto : acceptedApprovalDto.getFields()){
                Field field = new Field();
                field.setKey(fieldDto.getKey());
                field.setValue(fieldDto.getValue());
                field.setApproval(approval);
                field.setRequest(approval.getRequest());
                fieldRepository.save(field);
            }


            Request request = approval.getRequest();
            request.setStatus(RequestStatus.REJECTED);
            request.setLastModification(LocalDateTime.now());


            requestRepository.save(request);

            emailService.sendRejectedApproval(request, approval);


            int positionToRejected = acceptedApprovalDto.getPositionRejected();

            approval.setStatus(ApprovalStatus.PENDING);
            this.approvalRepository.save(approval);

            Approval prevApproval = this.getPrevAccepted(request);

            while (prevApproval.getPosition() > positionToRejected){
                 prevApproval.setStatus(ApprovalStatus.PENDING);
                 this.approvalRepository.save(prevApproval);
                 prevApproval = this.getPrevAccepted(request);
            }

            if(prevApproval != null){
                prevApproval.setStatus(ApprovalStatus.WAITING);
                this.approvalRepository.save(prevApproval);
                request.setApprobationLevel(approval.getPosition());
                this.emailService.sendAskApproval(request, prevApproval);
            }

            this.requestRepository.save(request);

            ApprovalDto dto = approvalMapper.toDto(approval);
            return new AppServiceResult<>(true, 0, "Succeed!", null);


        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<ApprovalDto>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    public Request rjectedRequestToInferiorApprobal(Request request, AcceptedApprovalDto acceptedApprovalDto){



        return request;
    }


    @Override
    public AppServiceResult<List<ApprovalListDto>> getApprovalByStaff(String staff, String status) {
        try {
            logger.info(MEMO_SERVICE + "newRequest : methode invocation");
            List<Approval> approvals = approvalRepository.findByStaffAndStatus(staff, ApprovalStatus.valueOf(status));
            List<Approval> approvalsUnity = approvalRepository.findApprovalsByUsernameInStaffListAndStatus(staff, ApprovalStatus.valueOf(status));

            approvals.addAll(approvalsUnity);


            List<ApprovalListDto> approvalDtos = new ArrayList<>();

            for (Approval approval : approvals) {
                ApprovalListDto dto = approvalListMapper.toDto(approval);
                RequestInfo info = requestInfoMapper.toDto(approval.getRequest());
                info.setDocumentType(approval.getRequest().getType().getName());
                dto.setRequest(info);
                if(approval.getType() == ApprovalType.STATIC){
                    dto.setFields(FieldUtils.getFieldsOfTypeAndPosition(approval.getRequest().getType().getStructure(), approval.getPosition()));
                }
                approvalDtos.add(dto);
            }

            return new AppServiceResult<List<ApprovalListDto>>(true, 0, "Succeed!", approvalDtos);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<List<ApprovalListDto>>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public AppServiceResult<ApprovalDto> getApprovalDetail(Long id) {
        try {
            logger.info(MEMO_SERVICE + "getApprovalDetail : methode invocation");

            Approval approval = approvalRepository.getOne(id);

            String type = approval.getRequest().getType().getStructure();

            ApprovalDto approvalDto = approvalMapper.toDto(approval);

            if(approvalDto.getType() == ApprovalType.STATIC){
                approvalDto.setFields(FieldUtils.getFieldsOfTypeAndPosition(type, approvalDto.getPosition()));
            }

            approvalDto.setRequestId(approval.getRequest().getId());

            RequestInfo dto = requestInfoMapper.toDto(approval.getRequest());

            dto.setDocumentType(approval.getRequest().getType().getName());

            approvalDto.setParent(dto);

            return new AppServiceResult<ApprovalDto>(true, 0, "Succeed!", approvalDto );


        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<ApprovalDto>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }



    private AppServiceResult<List<ApprovalDto>> getConvertedResult(List<Approval> approvals, String functionName) {
        if (approvals == null) {
            logger.warn(MEMO_SERVICE, functionName,
                    "Approval not exist!, Cannot further process!");
            return new AppServiceResult<List<ApprovalDto>>(false, AppError.Validattion.errorCode(),
                    "Approval not exist!", null);
        }
        List<ApprovalDto> result =  new ArrayList<ApprovalDto>();
        if (approvals.size() > 0) {
            for (Approval approval : approvals) {
                ApprovalDto dto = approvalMapper.toDto(approval);
                dto.setRequestId(approval.getRequest().getId());
                result.add(dto);
            }
        }
        return new AppServiceResult<List<ApprovalDto>>(true, 0, "Succeed!", result);
    }
}
