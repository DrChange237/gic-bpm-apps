package com.ccabank.paperless.controller;

import com.ccabank.paperless.dto.user.UserRestDto;
import com.ccabank.paperless.entity.ApprovalKey;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.openfeign.UserRestClient;
import com.ccabank.paperless.repository.ApprovalKeyRepository;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.ApprovalService;
import com.ccabank.paperless.service.faces.CamundaService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import org.springframework.ui.Model;


@Slf4j
@Controller
@RequestMapping("/paperless")
@RequiredArgsConstructor
public class FormValidationController {

    private final ApprovalKeyRepository approvalKeyRepository;

    private final UserRestClient userRestClient;

    private final CamundaService camundaService;

    private final RequestRepository requestRepository;

    private final ApprovalService approvalService;

    @Value("${base_url}")
    private String api ;



    @GetMapping("/validationForm")
    public String showForm(Model model, @RequestParam("key") String key, @RequestParam("taskId") String taskId, @RequestParam("reference") String reference) {

        ApprovalKey approvalKey = approvalKeyRepository.getOne(key);

        if(approvalKey == null){
            throw new NotFoundException("Approval key not found");
        }

        if(!approvalKey.getTaskId().equals(taskId)){
            throw new NotAuthorizedException("Approval key doesn't match");
        }

        UserRestDto employeeInfo = userRestClient.getAgencyByStaffUsername(approvalKey.getUsername() );

        model.addAttribute("user", employeeInfo.getName() );

        log.info("UserName Employe " + employeeInfo.getUsername());
        Task task = camundaService.getTaskDetails(approvalKey.getTaskId());

        Request request = requestRepository.findOneByReference(reference);
        employeeInfo = userRestClient.getAgencyByStaffUsername(request.getStaff());


        String  apiAccept = api + "validate?key="+ approvalKey.getId() +"&taskId="+ approvalKey.getTaskId() +"&reference="+ approvalKey.getReference() ;
        String  apiReject = api + "reject?key="+ approvalKey.getId() +"&taskId="+ approvalKey.getTaskId() +"&reference="+ approvalKey.getReference() ;


        model.addAttribute("type", request.getType().getName());
        model.addAttribute("approval", approvalKey);
        model.addAttribute("owner", employeeInfo.getName() );
        model.addAttribute("apiAccept", apiAccept);
        model.addAttribute("apiReject", apiReject);

        if(task != null){
            camundaService.setProcessVariable(task.getProcessInstanceId(), task.getId(), employeeInfo.getUsername());
            if(task.getAssignee() != null){
                if(!task.getAssignee().equals(employeeInfo.getUsername())){
                    throw new NotAuthorizedException("Vous n'etes pas autorisé à complete cette tâche");
                }
            }
            //camundaService.claimTask(task.getId(), employeeInfo.getUsername());
            model.addAttribute("name", task.getName());

            return "commentForm";
        }

        HistoricTaskInstance taskInstance = camundaService.getHistoryTaskInstance(taskId);
        model.addAttribute("name", taskInstance.getName());
        employeeInfo = userRestClient.getAgencyByStaffUsername(taskInstance.getAssignee());
        model.addAttribute("collaborator", employeeInfo.getName() );

        return "already";

    }

    @GetMapping("/validate")
    public String decisionAccept(Model model,@RequestParam("key") String key, @RequestParam("taskId") String taskId,  @RequestParam("reference") String reference) {

        ApprovalKey approvalKey = approvalKeyRepository.getOne(key);

        if(approvalKey == null){
            model.addAttribute("error", "Approval key not found");
            return "error";
        }

        if(!approvalKey.getTaskId().equals(taskId)){
            model.addAttribute("error", "Approval key doesn't match");
            return "error";
        }

        UserRestDto employeeInfo = userRestClient.getAgencyByStaffUsername(approvalKey.getUsername());
        model.addAttribute("user", employeeInfo.getName());

        log.info("UserName Employe " + employeeInfo.getUsername());
        Task task = camundaService.getTaskDetails(approvalKey.getTaskId());

        Request request = requestRepository.findOneByReference(reference);
        employeeInfo = userRestClient.getAgencyByStaffUsername(request.getStaff());

        model.addAttribute("type", request.getType().getName());
        model.addAttribute("approval", approvalKey);
        model.addAttribute("owner", employeeInfo.getName());

        if(task != null){
            camundaService.setProcessVariable(task.getProcessInstanceId(), task.getId(), employeeInfo.getUsername());
            if(task.getAssignee() != null){
                if(!task.getAssignee().equals(employeeInfo.getUsername())){
                    model.addAttribute("error", "Vous n'etes pas autorisé à complete cette tâche");
                    return "error";
                }
            }
            //camundaService.claimTask(task.getId(), employeeInfo.getUsername());
            model.addAttribute("name", task.getName());
            try {
                approvalService.decisionViaEmail(key, taskId, true, "");
            }catch (Exception e){
                model.addAttribute("error", e.getMessage());
                return "error";
            }
            return "success";
        }

        HistoricTaskInstance taskInstance = camundaService.getHistoryTaskInstance(taskId);
        model.addAttribute("name", taskInstance.getName());
        employeeInfo = userRestClient.getAgencyByStaffUsername(taskInstance.getAssignee());
        model.addAttribute("collaborator", employeeInfo.getName());

        return "already";
    }



    @GetMapping("/reject")
    public String decisionReject(Model model, @RequestParam("key") String key, @RequestParam("taskId") String taskId, @RequestParam("reference") String reference, @RequestParam("comment") String comment) {
        ApprovalKey approvalKey = approvalKeyRepository.getOne(key);

        if(!approvalKey.getTaskId().equals(taskId)){
            throw new NotAuthorizedException("Approval key doesn't match");
        }

        UserRestDto employeeInfo = userRestClient.getAgencyByStaffUsername(approvalKey.getUsername());
        model.addAttribute("user", employeeInfo.getName());

        log.info("UserName Employe " + employeeInfo.getUsername());
        Task task = camundaService.getTaskDetails(approvalKey.getTaskId());

        Request request = requestRepository.findOneByReference(reference);
        employeeInfo = userRestClient.getAgencyByStaffUsername(request.getStaff());

        model.addAttribute("type", request.getType().getName());
        model.addAttribute("approval", approvalKey);
        model.addAttribute("owner", employeeInfo.getName());

        if(task != null){
            camundaService.setProcessVariable(task.getProcessInstanceId(), task.getId(), employeeInfo.getUsername());
            if(task.getAssignee() != null){
                if(!task.getAssignee().equals(employeeInfo.getUsername())){
                    throw new NotAuthorizedException("Vous n'etes pas autorisé à complete cette tâche");
                }
            }
            camundaService.claimTask(task.getId(), employeeInfo.getUsername());
            model.addAttribute("name", task.getName());
            approvalService.decisionViaEmail(key, taskId, false, comment);
            return "reject";
        }

        HistoricTaskInstance taskInstance = camundaService.getHistoryTaskInstance(taskId);
        model.addAttribute("name", taskInstance.getName());
        employeeInfo = userRestClient.getAgencyByStaffUsername(taskInstance.getAssignee());
        model.addAttribute("collaborator", employeeInfo.getName());

        return "already";
    }


}
