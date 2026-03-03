package com.change.gic.modules.business.process.collaborator.listener;


import com.change.gic.modules.core.dto.auth.RegisterUserDto;
import com.change.gic.modules.core.service.faces.EmailService;
import com.change.gic.modules.core.service.faces.UserService;
import com.change.gic.modules.core.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateCollaboratorListener implements ExecutionListener {

    private final UserService userService;
    private final EmailService emailService;


    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String username = (String) delegateExecution.getVariable("username");
        String email = (String) delegateExecution.getVariable("email");
        String agency = (String) delegateExecution.getVariable("agency");
        String role = (String) delegateExecution.getVariable("role");
        String password = (String) delegateExecution.getVariable("password");

        //String password = PasswordGenerator.generatePassword(10);
        delegateExecution.setVariable("password", password);
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername(username);
        registerUserDto.setEmail(email);
        registerUserDto.setRole(role);
        registerUserDto.setAgencyId(agency);
        registerUserDto.setPassword(password);
        userService.createUser(registerUserDto);

        emailService.sendSimpleMail(email, "GIC - Votre Mot de Passe", password);

    }
}
