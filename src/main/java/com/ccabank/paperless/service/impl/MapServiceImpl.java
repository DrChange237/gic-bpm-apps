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

import javax.ws.rs.BadRequestException;
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


            String taskId = historic.getId();

            ApprovalDto approvalDto = new ApprovalDto();
            approvalDto.setType(ApprovalType.STATIC);
            approvalDto.setStatus(ApprovalStatus.PENDING);

            if(taskId != null){
                approvalDto.setType(ApprovalType.OPEN);
                approvalDto.setId(historic.getId());
                approvalDto.setRole(historic.getName());
                approvalDto.setPosition(historics.indexOf(historic));
                String natureTask = camundaService.getTaskAssigneeNature(taskId);

                if(natureTask.equals("GROUP")){
                    approvalDto.setType(ApprovalType.STATIC);
                    approvalDto.setHaveSignature(false);
                }

                if(historic.getAssignee() != null){
                    try{
                        approvalDto.setHaveSignature(securityService.checkUserSignature(historic.getAssignee()));
                    }catch (Exception e){
                        approvalDto.setHaveSignature(false);
                    }
                    approvalDto.setStaff(historic.getAssignee());
                }

                if(historic.getEndTime() != null){
                    approvalDto.setApprovalDate(DateUtil.convertDateToLocalDateTime(historic.getEndTime()));
                    approvalDto.setTime(DateUtil.timeAgo(DateUtil.convertDateToLocalDateTime(historic.getEndTime())));
                }
            }

            if(historic.getDurationInMillis() != null){
                if(historic.getDurationInMillis() > 0){
                    approvalDto.setStatus(ApprovalStatus.WAITING);
                }
            }

            if(historic.getEndTime() != null){
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
