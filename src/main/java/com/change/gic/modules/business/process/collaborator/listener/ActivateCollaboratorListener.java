package com.change.gic.modules.business.process.collaborator.listener;

import com.change.gic.modules.core.service.faces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ActivateCollaboratorListener implements ExecutionListener {

    private final UserService userService;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String username = (String) delegateExecution.getVariable("username");
        userService.activateUser(username);

    }
}
