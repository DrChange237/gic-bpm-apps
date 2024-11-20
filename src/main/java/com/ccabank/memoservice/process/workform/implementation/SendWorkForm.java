package com.ccabank.memoservice.process.workform.implementation;

import com.ccabank.memoservice.dto.email.AttachmentDto;
import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.reporting.WorkForm;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.ccabank.memoservice.openfeign.ReportingRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.process.general.service.RequestService;
import com.ccabank.memoservice.service.faces.CamundaService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Component
public class SendWorkForm implements JavaDelegate {

    @Autowired
    private ReportingRestClient reportingRestClient;

    @Autowired
    private EmailRestClient emailRestClient;

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private CamundaService camundaService;

    @Autowired
    private RequestService requestService;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        WorkForm form = new WorkForm();

        String owner = (String) delegateExecution.getVariable("owner");

        EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);
        form.setDate(LocalDate.now());
        form.setUnity(staff.getDepartment().getName());

        WorkForm.Signatory staffSignatory = new WorkForm.Signatory();
        staffSignatory.setDate(LocalDate.now());
        staffSignatory.setName(staff.getFirstName() + " " + staff.getLastName());
        staffSignatory.setSignature(userRestClient.getEmployeeSignature(staff.getUsername()));
        form.setInitiator(staffSignatory);

        String Apbt_n1 = (String) delegateExecution.getVariable("Apbt_n1");
        EmployeeInfo supervisor =  userRestClient.getStaffByUsername(Apbt_n1);
        WorkForm.Signatory supervisorSignatory = new WorkForm.Signatory();
        supervisorSignatory.setDate(LocalDate.now());
        supervisorSignatory.setName(supervisor.getFirstName() + " " + supervisor.getLastName());
        supervisorSignatory.setSignature(userRestClient.getEmployeeSignature(supervisor.getUsername()));
        form.setSupervisor(supervisorSignatory);


        String Apbt_n2 = (String) delegateExecution.getVariable("Apbt_n2");
        EmployeeInfo supervisor2 =  userRestClient.getStaffByUsername(Apbt_n2);
        WorkForm.Signatory supervisor2Signatory = new WorkForm.Signatory();
        supervisor2Signatory.setDate(LocalDate.now());
        supervisor2Signatory.setName(supervisor2.getFirstName() + " " + supervisor2.getLastName());
        supervisor2Signatory.setSignature(userRestClient.getEmployeeSignature(supervisor2.getUsername()));
        form.setDepartment(supervisor2Signatory);

        Optional<HistoricTaskInstance> taskAccountant = camundaService.getLastHistoricTaskInstance(delegateExecution.getProcessInstanceId(), "Apbt_Accountant");

        if(taskAccountant.isPresent()) {
            String Apbt_Accountant = taskAccountant.get().getAssignee();
            EmployeeInfo accountant =  userRestClient.getStaffByUsername(Apbt_Accountant);
            WorkForm.Signatory accountantSignatory = new WorkForm.Signatory();
            accountantSignatory.setDate(LocalDate.now());
            accountantSignatory.setName(accountant.getFirstName() + " " + accountant.getLastName());
            accountantSignatory.setSignature(userRestClient.getEmployeeSignature(accountant.getUsername()));
            form.setAccountant(accountantSignatory);
        }


        String provider = (String) delegateExecution.getVariable("provider");
        form.setProvider(provider);

        String designation = (String) delegateExecution.getVariable("designation");
        form.setDesignation(designation);

        String brand = (String) delegateExecution.getVariable("brand");
        form.setBrand(brand);

        String code = (String) delegateExecution.getVariable("code");
        form.setLabelCode(code);

        form.setUser(staff.getFirstName() + " " + staff.getLastName());

        String works = (String) delegateExecution.getVariable("works");
        List<String> tasks = List.of(works.toUpperCase().split(","));
        form.setTasks(tasks);

        ByteArrayResource resource = this.reportingRestClient.workform(form);

        EmailDto emailDto = new EmailDto();
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setTo(staff.getEmail());
        emailDto.setSubject("Demande de Travail");
        emailDto.setCc(staff.getEmail());
        emailDto.setBody("En pièce jointe la demande de travail");
        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("demande_travail.pdf");
        attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
        emailDto.setAttachments(new AttachmentDto[]{attachment});
        this.emailRestClient.send(emailDto);

        requestService.confirmRequest(delegateExecution.getProcessInstanceId());

    }
}
