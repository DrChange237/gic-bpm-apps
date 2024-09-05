package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.reporting.HandOverForm;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.Vacation;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.VacationRepository;
import com.ccabank.memoservice.service.faces.HandOverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;

@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class HandOverServiceImpl implements HandOverService {


    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private UserRestClient userRestClient;

    @Override
    public HandOverForm construct(Request request){

        HandOverForm handOverForm = new HandOverForm();
        Vacation vacation = vacationRepository.getOne(request.getDocumentId());

        HandOverForm.Employee employee = new HandOverForm.Employee();
        employee.setDate(vacation.getDate());
        employee.setName(vacation.getRequester().getName());
        employee.setFunction(vacation.getRequester().getFunction());
        String signature = userRestClient.getEmployeeSignature(vacation.getRequester().getUsername());
        employee.setSignature(signature);

        handOverForm.setEmployee(employee);
        handOverForm.setStartDate(vacation.getStartDate());
        handOverForm.setEndDate(vacation.getEndDate());


        HandOverForm.Employee interim = new HandOverForm.Employee();
        employee.setDate(vacation.getDate());
        employee.setName(vacation.getInterim().getName());
        employee.setFunction(vacation.getInterim().getFunction());
        signature = userRestClient.getEmployeeSignature(vacation.getInterim().getUsername());
        employee.setSignature(signature);

        handOverForm.setInterim(interim);


        HandOverForm.Employee supervisor = new HandOverForm.Employee();
        employee.setDate(vacation.getDate());
        employee.setName(vacation.getSupervisor().getOwner().getName());
        employee.setFunction(vacation.getSupervisor().getOwner().getFunction());
        signature = userRestClient.getEmployeeSignature(vacation.getSupervisor().getOwner().getUsername());
        employee.setSignature(signature);

        handOverForm.setSupervisor(supervisor);


        handOverForm.setActivities(vacation.getCriticFolder());
        handOverForm.setResponsibilities(vacation.getMainWork());


         return  handOverForm;

    }

}
