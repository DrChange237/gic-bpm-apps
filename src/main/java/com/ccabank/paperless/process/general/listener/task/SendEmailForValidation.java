package com.ccabank.paperless.process.general.listener.task;


import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
import com.ccabank.paperless.dto.memo.ApprovalDto;
import com.ccabank.paperless.dto.memo.FieldDto;
import com.ccabank.paperless.entity.ApprovalKey;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.repository.ApprovalKeyRepository;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.EmailService;
import com.ccabank.paperless.service.faces.MapService;
import com.ccabank.paperless.util.camunda.Mapping;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.IdentityLink;
import org.hibernate.annotations.ValueGenerationType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SendEmailForValidation  implements TaskListener {

    private final EmailService emailService;
    private final CamundaService camundaService;
    private final RequestRepository requestRepository;
    private final IdentityService identityService;
    private final ApprovalKeyRepository approvalKeyRepository;
    private final MapService mapService;



    @Override
    public void notify(DelegateTask delegateTask) {

        System.out.println("SendEmailForValidation Task Listener");
        List<String> candidateUsers = getCandidateUserIds(delegateTask);
        EmailAskApprovalDto ask = new EmailAskApprovalDto();

        String processDefinitionId = delegateTask.getProcessDefinitionId();
        ProcessDefinition definition = camundaService.getProcessDefinition(processDefinitionId);
        String owner = (String) delegateTask.getVariable("owner");
        String reference = (String) delegateTask.getVariable("reference");
        ask.setSender(owner);
        ask.setReference(reference);
        Request request = requestRepository.findOneByReference(reference);
        ask.setType(request.getType().getName());

        ask.setSubject("[Action Requise] Approbation de  - " + definition.getName());
        ask.setRole(delegateTask.getName());

        StartFormData formData = camundaService.getStartForm(request.getType().getStructure());
        Map<String, Object> variables = camundaService.getProcessVariables(request.getInstanceId());

        System.out.println("Recupération des Champs");
        List<FieldDto> fields = Mapping.getFieldFromFormField(formData, variables);

        System.out.println("Recupération des Approbations");
        List<HistoricTaskInstance> histories = camundaService.getHistoricTasksForProcessInstance(request.getInstanceId());
        List<ApprovalDto> approvalDtos = this.mapService.mapTaskToApprovalDto(histories);


        System.out.println(candidateUsers);

        for (String user : candidateUsers) {

            ApprovalKey approvalKey = new ApprovalKey();
            approvalKey.setUsername(user);
            approvalKey.setTaskId(delegateTask.getId());
            approvalKey.setReference(request.getReference());
            ask.setApprover(user);

            approvalKeyRepository.save(approvalKey);
            System.out.println("Envoi de mail a " + user);
            emailService.sendForValidation(approvalKey, ask, fields, approvalDtos);
        }

    }

    public List<String> getCandidateUserIds(DelegateTask delegateTask) {
        Set<IdentityLink> candidates = delegateTask.getCandidates();
        Set<String> userIds = new HashSet<>();

        // Récupération des userIds des utilisateurs
        for (IdentityLink link : candidates) {
            if (link.getUserId() != null) {
                userIds.add(link.getUserId());
            } else if (link.getGroupId() != null) {
                // Ajout des utilisateurs du groupe
                List<User> groupMembers = identityService.createUserQuery()
                        .memberOfGroup(link.getGroupId())
                        .list();
                userIds.addAll(groupMembers.stream()
                        .map(User::getId)
                        .collect(Collectors.toSet()));
            }
        }

        return List.copyOf(userIds);
    }
}
