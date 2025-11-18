package com.ccabank.paperless.service.impl;

import com.ccabank.paperless.dto.memo.ProcessUnityDto;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.ProcessUnityService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@Transactional
public class ProcessUnityServiceImpl implements ProcessUnityService {

    @Autowired
    private CamundaService camundaService;
    

    @Override
    @Transactional
    public AppServiceResult<ProcessUnityDto> create(ProcessUnityDto processUnityDto) {
        try {
            log.info("create : methode invocation");

            camundaService.createGroup(processUnityDto.getCode(), processUnityDto.getName(),"PROCESS-UNITY");

            List<String> staffList = List.of(processUnityDto.getStaffList().split(";"));

            for (String staff : staffList){
                 camundaService.addUserToGroup(staff, processUnityDto.getCode());
            }

            return new AppServiceResult<ProcessUnityDto>(true, 0, "Succeed!", processUnityDto );

        } catch (Exception e) {

            return new AppServiceResult<ProcessUnityDto>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    @Transactional
    public AppServiceResult<ProcessUnityDto> update(ProcessUnityDto processUnityDto) {
        try {
            log.info("update : methode invocation");

            //ProcessUnity processUnity = processUnityRepository.findById(processUnityDto.getId()).orElse(null);

            Group group = camundaService.getGroup(processUnityDto.getCode());

            if (group == null) {
                log.warn("update : process unity not found -> " + processUnityDto.getId());
                return new AppServiceResult<ProcessUnityDto>(false, HttpStatus.NOT_FOUND.value(), "process unity not found!", null);
            }

            camundaService.updateGroup(group.getId(), processUnityDto.getName(),"");
            camundaService.updateGroupMembers(group.getId(), List.of(processUnityDto.getStaffList().split(";")));


            return new AppServiceResult<ProcessUnityDto>(true, 0, "Succeed!", processUnityDto );

        } catch (Exception e) {
            e.printStackTrace();
            log.error(" create : Exception {}", e.getMessage());
            return new AppServiceResult<ProcessUnityDto>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public AppServiceResult<ProcessUnityDto> getDetail(Long id) {
        try {
            log.info("getDetail : methode invocation");

            //ProcessUnity unity = processUnityRepository.getOne(id);

            ProcessUnityDto unityDto = new ProcessUnityDto();

            return new AppServiceResult<ProcessUnityDto>(true, 0, "Succeed!", unityDto );

        } catch (Exception e) {
            e.printStackTrace();
            log.error(" create : Exception {}", e.getMessage());
            return new AppServiceResult<ProcessUnityDto>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public String getEmailUnity(String unityCode) {
            log.info("getEmailUnity : methode invocation");
            Group group = camundaService.getGroup(unityCode);
            List<User> members = camundaService.getGroupDetailsWithMembers(group.getId());
            log.info("Membres : " + members.size());
            String staffList = "";
            for(User m : members){
                if(members.indexOf(m) == 0){
                    staffList = staffList +  m.getEmail() ;
                }else{
                    staffList =  staffList + "," +  m.getEmail() ;
                }
            }
            return staffList.replace(",,",",");
    }



    @Override
    public AppServiceResult<List<ProcessUnityDto>> getAll() {
        try {
            log.info("getAll : methode invocation");

            List<Group> groups =   camundaService.getAllGroup();

            //List<ProcessUnity> unities = processUnityRepository.findAll();

            List<ProcessUnityDto> unityDtos = this.getConvertedResult(groups, "getAll").getData();

            return new AppServiceResult<List<ProcessUnityDto>>(true, 0, "Succeed!", unityDtos );

        } catch (Exception e) {
            e.printStackTrace();
            log.error(" create : Exception {}", e.getMessage());
            return new AppServiceResult<List<ProcessUnityDto>>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    private AppServiceResult<List<ProcessUnityDto>> getConvertedResult(List<Group> groups, String functionName) {
        if (groups == null) {
            log.warn(MEMO_SERVICE, functionName,
                    "Feedback not exist!, Cannot further process!");
            return new AppServiceResult<List<ProcessUnityDto>>(false, AppError.Validation.errorCode(),
                    "Process Unity not exist!", null);
        }
        List<ProcessUnityDto> result =  new ArrayList<ProcessUnityDto>();
        if (groups.size() > 0) {
            for (Group group : groups) {
                ProcessUnityDto processUnityDto = new ProcessUnityDto();
                processUnityDto.setCode(group.getId());
                processUnityDto.setName(group.getName());

                List<User> members = camundaService.getGroupDetailsWithMembers(group.getId());

                log.info("Membres : " + members.size()  );

                String staffList = "";
                for(User m : members){
                    staffList =  m.getEmail().replace("@cca-bank.com", "") + ";" + staffList;
                }
                processUnityDto.setStaffList(staffList);
                result.add(processUnityDto);
            }
        }
        return new AppServiceResult<List<ProcessUnityDto>>(true, 0, "Succeed!", result);
    }



}
