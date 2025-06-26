package com.ccabank.paperless.service.impl;

import com.ccabank.paperless.dto.memo.ApprovalDto;
import com.ccabank.paperless.entity.Approbation;
import com.ccabank.paperless.entity.ApprovalStatus;
import com.ccabank.paperless.entity.ApprovalType;
import com.ccabank.paperless.openfeign.UserRestClient;
import com.ccabank.paperless.repository.ApprobationRepository;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.MapService;
import com.ccabank.paperless.service.faces.SecurityService;
import com.ccabank.paperless.util.DateUtil;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MapServiceImpl implements MapService {

    @Autowired
    private CamundaService camundaService;

    @Autowired
    private ApprobationRepository approbationRepository;

    @Autowired
    private SecurityService securityService;

    @Override
    public List<ApprovalDto> mapTaskToApprovalDto(List<HistoricTaskInstance> historics) {

        List<ApprovalDto> approvalDtos = new ArrayList<>();

        for (HistoricTaskInstance historic : historics) {

            System.out.println(historic);

            String taskId = historic.getId();

            ApprovalDto approvalDto = new ApprovalDto();
            approvalDto.setType(ApprovalType.STATIC);
            approvalDto.setStatus(ApprovalStatus.PENDING);

            if(taskId != null){
                approvalDto.setType(ApprovalType.OPEN);
                System.out.println("Task Id : " + taskId);
                approvalDto.setId(historic.getId());
                System.out.println("ActivitiName : " + historic.getName());
                approvalDto.setRole(historic.getName());
                System.out.println("Position : " + historics.indexOf(historic));
                approvalDto.setPosition(historics.indexOf(historic));
                String natureTask = camundaService.getTaskAssigneeNature(taskId);
                if(natureTask.equals("GROUP")){
                    approvalDto.setType(ApprovalType.STATIC);
                }

                if(historic.getAssignee() != null){
                    System.out.println("Assigne : " + historic.getAssignee());
                    approvalDto.setHaveSignature(securityService.checkUserSignature(historic.getAssignee()));
                    approvalDto.setStaff(historic.getAssignee());
                }

                if(historic.getEndTime() != null){
                    System.out.println("EndTime : " + historic.getAssignee());
                    approvalDto.setApprovalDate(DateUtil.convertDateToLocalDateTime(historic.getEndTime()));
                    approvalDto.setTime(DateUtil.timeAgo(DateUtil.convertDateToLocalDateTime(historic.getEndTime())));
                }
            }

            if(historic.getDurationInMillis() != null){
                System.out.println("Duration : " + historic.getDurationInMillis());
                if(historic.getDurationInMillis() > 0){
                    approvalDto.setStatus(ApprovalStatus.WAITING);
                }
            }

            if(historic.getEndTime() != null){
                System.out.println("Is Complete : ");
                if(historic.getId() != null){
                    Optional<Approbation> approbationOptional = approbationRepository.findByTaskId(historic.getId());
                    if(approbationOptional.isPresent()){
                        Approbation approbation = approbationOptional.get();
                        approvalDto.setStatus(approbation.getStatus());
                        approvalDto.setComments(approbation.getComments());
                    }else{
                        approvalDto.setStatus(ApprovalStatus.ACCEPTED);
                    }
                }
            }

            approvalDto.setFields(new ArrayList<>());
            approvalDtos.add(approvalDto);
        }

        return approvalDtos;
    }
}
