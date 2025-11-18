package com.ccabank.paperless.process.general.listener.task;

import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.IdentityLink;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendEmailGroupListener implements TaskListener {

    private final EmailService emailService;
    private final CamundaService camundaService;
    private final RequestRepository requestRepository;
    private final IdentityService identityService;


    @Override
    public void notify(DelegateTask delegateTask) {


        log.info("SendEmailGroupListener Task Listener");

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

        ask.setSubject("Demande d'approbation - " + definition.getName());
        ask.setRole(delegateTask.getName());

        log.info(candidateUsers.toString());

        for (String user : candidateUsers) {
            log.info("Envoi de mail a " + user);
            ask.setApprover(user);
            emailService.sendAskApproval(ask);
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
