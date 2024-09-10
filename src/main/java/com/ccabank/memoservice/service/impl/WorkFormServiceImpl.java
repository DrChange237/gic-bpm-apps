package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.WorkForm;
import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import com.ccabank.memoservice.entity.documenttype.sub.Staff;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.SignatoryRepository;
import com.ccabank.memoservice.repository.StaffRepository;
import com.ccabank.memoservice.repository.WorkFormRepository;
import com.ccabank.memoservice.service.faces.SignatoryService;
import com.ccabank.memoservice.service.faces.WorkformService;
import com.ccabank.memoservice.util.field.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;

@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class WorkFormServiceImpl implements WorkformService {

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private SignatoryRepository signatoryRepository;

    @Autowired
    private SignatoryService signatoryService;

    @Autowired
    private WorkFormRepository workFormRepository;

    @Override
    public WorkForm save(Request request){

        WorkForm workForm = new WorkForm();

        workForm.setDate(LocalDate.now());

        UserRestDto staff = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");


        Staff requester = new Staff();
        requester.setFunction(staff.getFunction());
        requester.setName(staff.getName());
        requester.setUnity(staff.getDepartment());
        requester.setMatricule(staff.getMatricule());
        requester.setUsername(request.getStaff());

        requester = staffRepository.save(requester);
        workForm.setRequester(requester);

        Signatory owner = new Signatory();
        owner.setOwner(requester);
        //String signature = userRestClient.getEmployeeSignature(staff.getUsername());
        owner.setSignature("");

        owner = signatoryRepository.save(owner);
        workForm.setOwner(owner);

        //Work
        String works = FieldUtils.getValueOfField(request,"works");
        workForm.setWorkToSolve(works);

        //Provider
        String provider = FieldUtils.getValueOfField(request,"provider");
        workForm.setProvider(provider);

        //Designation
        String designation = FieldUtils.getValueOfField(request,"designation");
        workForm.setDesignation(designation);

        //Brand
        String brand = FieldUtils.getValueOfField(request,"brand");
        workForm.setBrand(designation);

        //Code
        String code = FieldUtils.getValueOfField(request,"code");
        workForm.setCode(code);

        //Supervisor
        String username = request.getApprovalByPosition(1).getStaff();
        Signatory supervisor = signatoryService.getSignatory(username);
        workForm.setSupervisor(supervisor);

        //Head
        String head = request.getApprovalByPosition(2).getStaff();
        Signatory headSignatory = signatoryService.getSignatory(username);
        workForm.setHead(headSignatory);

        //Accountant
        String accountant = request.getApprovalByPosition(3).getStaff();
        Signatory accountantSignatory = signatoryService.getSignatory(username);
        workForm.setAccountant(accountantSignatory);

        this.workFormRepository.save(workForm);

        return  workForm;
    }

    @Override
    public com.ccabank.memoservice.dto.reporting.WorkForm construct(Request request){

        com.ccabank.memoservice.entity.documenttype.WorkForm workForm = workFormRepository.getOne(request.getDocumentId());
        com.ccabank.memoservice.dto.reporting.WorkForm workForm1 = new com.ccabank.memoservice.dto.reporting.WorkForm();
        workForm1.setDate(workForm.getDate());


        workForm1.setUnity(workForm.getRequester().getUnity());

        workForm1.setUser(workForm.getRequester().getName());


        com.ccabank.memoservice.dto.reporting.WorkForm.Signatory signatory = new com.ccabank.memoservice.dto.reporting.WorkForm.Signatory();

        signatory.setName(workForm.getRequester().getName());
        signatory.setDate(workForm.getDate());

        String signature = userRestClient.getEmployeeSignature(workForm.getRequester().getUsername());

        signatory.setSignature(signature);

        workForm1.setInitiator(signatory);

        //Supervisor
        signatory = new com.ccabank.memoservice.dto.reporting.WorkForm.Signatory();
        signatory.setName(workForm.getSupervisor().getOwner().getName());
        signature = userRestClient.getEmployeeSignature(workForm.getSupervisor().getOwner().getUsername());
        signatory.setSignature(signature);
        workForm1.setSupervisor(signatory);


        //SupervisorNext
        signatory = new com.ccabank.memoservice.dto.reporting.WorkForm.Signatory();
        signatory.setName(workForm.getHead().getOwner().getName());
        signature = userRestClient.getEmployeeSignature(workForm.getHead().getOwner().getUsername());
        signatory.setSignature(signature);
        workForm1.setDepartment(signatory);

        //SupervisorNext
        signatory = new com.ccabank.memoservice.dto.reporting.WorkForm.Signatory();
        signatory.setName(workForm.getHead().getOwner().getName());
        signature = userRestClient.getEmployeeSignature(workForm.getAccountant().getOwner().getUsername());
        signatory.setSignature(signature);
        workForm1.setAccountant(signatory);



        workForm1.setBrand(workForm.getBrand());
        workForm1.setDesignation(workForm.getDesignation());
        workForm1.setLabelCode(workForm.getCode());
        workForm1.setProvider(workForm.getProvider());

        // Convertir la chaîne en tableau
        String[] array = workForm.getWorkToSolve().split(",");

        // Convertir le tableau en liste
        List<String> list = Arrays.asList(array);

        workForm1.setTasks(list);

        return workForm1;

    }

}
