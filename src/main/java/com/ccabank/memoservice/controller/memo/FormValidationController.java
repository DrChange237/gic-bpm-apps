package com.ccabank.memoservice.controller.memo;


import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.HttpResponseError;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.entity.ApprovalKey;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.ApprovalKeyRepository;
import com.ccabank.memoservice.repository.RequestRepository;
import com.ccabank.memoservice.service.faces.ApprovalService;
import com.ccabank.memoservice.service.faces.CamundaService;
import io.swagger.annotations.Api;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import org.springframework.ui.Model;



@Controller
@Api(tags = "Paperless")
@RequestMapping("/paperless")
public class FormValidationController {

    @Autowired
    private ApprovalKeyRepository approvalKeyRepository;

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private CamundaService camundaService;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ApprovalService approvalService;

    @Value("${base_url}")
    private String api ;


    @GetMapping("/empty")
    public String empty() {
          camundaService.stopAllActiveProcessInstances();
          return "test";
    }

    @GetMapping("/validationForm")
    public String showForm(Model model, @RequestParam("key") String key, @RequestParam("taskId") String taskId, @RequestParam("reference") String reference) {

        ApprovalKey approvalKey = approvalKeyRepository.getOne(key);

        if(approvalKey == null){
            throw new NotFoundException("Approval key not found");
        }

        if(!approvalKey.getTaskId().equals(taskId)){
            throw new NotAuthorizedException("Approval key doesn't match");
        }

        EmployeeInfo employeeInfo = userRestClient.getStaffByUsername(approvalKey.getUsername());
        model.addAttribute("user", employeeInfo.getFirstName() + " " + employeeInfo.getLastName());

        System.out.println("UserName Employe " + employeeInfo.getUsername());
        Task task = camundaService.getTaskDetails(approvalKey.getTaskId());

        Request request = requestRepository.findOneByReference(reference);
        employeeInfo = userRestClient.getStaffByUsername(request.getStaff());

        String  apiAccept = api + "validate?key="+ approvalKey.getId() +"&taskId="+ approvalKey.getTaskId() +"&reference="+ approvalKey.getReference() ;
        String  apiReject = api + "reject?key="+ approvalKey.getId() +"&taskId="+ approvalKey.getTaskId() +"&reference="+ approvalKey.getReference() ;


        model.addAttribute("type", request.getType().getName());
        model.addAttribute("approval", approvalKey);
        model.addAttribute("owner", employeeInfo.getFirstName() + " " + employeeInfo.getLastName());
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
        employeeInfo = userRestClient.getStaffByUsername(taskInstance.getAssignee());
        model.addAttribute("collaborator", employeeInfo.getFirstName() + " " + employeeInfo.getLastName());

        return "already";

    }

    @GetMapping("/validate")
    public String decisionAccept(Model model,@RequestParam("key") String key, @RequestParam("taskId") String taskId,  @RequestParam("reference") String reference) {

        ApprovalKey approvalKey = approvalKeyRepository.getOne(key);
        if(approvalKey == null){
            throw new NotFoundException("Approval key not found");
        }

        if(!approvalKey.getTaskId().equals(taskId)){
            throw new NotAuthorizedException("Approval key doesn't match");
        }

        EmployeeInfo employeeInfo = userRestClient.getStaffByUsername(approvalKey.getUsername());
        model.addAttribute("user", employeeInfo.getFirstName() + " " + employeeInfo.getLastName());

        System.out.println("UserName Employe " + employeeInfo.getUsername());
        Task task = camundaService.getTaskDetails(approvalKey.getTaskId());

        Request request = requestRepository.findOneByReference(reference);
        employeeInfo = userRestClient.getStaffByUsername(request.getStaff());

        model.addAttribute("type", request.getType().getName());
        model.addAttribute("approval", approvalKey);
        model.addAttribute("owner", employeeInfo.getFirstName() + " " + employeeInfo.getLastName());

        if(task != null){
            camundaService.setProcessVariable(task.getProcessInstanceId(), task.getId(), employeeInfo.getUsername());
            if(task.getAssignee() != null){
                if(!task.getAssignee().equals(employeeInfo.getUsername())){
                    throw new NotAuthorizedException("Vous n'etes pas autorisé à complete cette tâche");
                }
            }
            camundaService.claimTask(task.getId(), employeeInfo.getUsername());
            model.addAttribute("name", task.getName());
            approvalService.decisionViaEmail(key, taskId, true, "");
            return "success";
        }

        HistoricTaskInstance taskInstance = camundaService.getHistoryTaskInstance(taskId);
        model.addAttribute("name", taskInstance.getName());
        employeeInfo = userRestClient.getStaffByUsername(taskInstance.getAssignee());
        model.addAttribute("collaborator", employeeInfo.getFirstName() + " " + employeeInfo.getLastName());

        return "already";
    }



    @GetMapping("/reject")
    public String decisionReject(Model model, @RequestParam("key") String key, @RequestParam("taskId") String taskId, @RequestParam("reference") String reference, @RequestParam("comment") String comment) {
        ApprovalKey approvalKey = approvalKeyRepository.getOne(key);

        if(!approvalKey.getTaskId().equals(taskId)){
            throw new NotAuthorizedException("Approval key doesn't match");
        }

        EmployeeInfo employeeInfo = userRestClient.getStaffByUsername(approvalKey.getUsername());
        model.addAttribute("user", employeeInfo.getFirstName() + " " + employeeInfo.getLastName());

        System.out.println("UserName Employe " + employeeInfo.getUsername());
        Task task = camundaService.getTaskDetails(approvalKey.getTaskId());

        Request request = requestRepository.findOneByReference(reference);
        employeeInfo = userRestClient.getStaffByUsername(request.getStaff());

        model.addAttribute("type", request.getType().getName());
        model.addAttribute("approval", approvalKey);
        model.addAttribute("owner", employeeInfo.getFirstName() + " " + employeeInfo.getLastName());

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
        employeeInfo = userRestClient.getStaffByUsername(taskInstance.getAssignee());
        model.addAttribute("collaborator", employeeInfo.getFirstName() + " " + employeeInfo.getLastName());

        return "already";
    }


}
