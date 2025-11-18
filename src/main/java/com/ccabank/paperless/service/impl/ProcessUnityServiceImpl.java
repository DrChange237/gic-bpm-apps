package com.ccabank.paperless.service.impl;

import com.ccabank.paperless.dto.memo.ProcessUnityDto;
import com.ccabank.paperless.exception.NotFoundException;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.ProcessUnityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProcessUnityServiceImpl implements ProcessUnityService {

    private final CamundaService camundaService;
    

    @Override
    @Transactional
    public ProcessUnityDto create(ProcessUnityDto processUnityDto) {
        log.info("create : methode invocation");

        camundaService.createGroup(processUnityDto.getCode(), processUnityDto.getName(),"PROCESS-UNITY");

        List<String> staffList = List.of(processUnityDto.getStaffList().split(";"));

        for (String staff : staffList){
            camundaService.addUserToGroup(staff, processUnityDto.getCode());
        }

        return processUnityDto;
    }

    @Override
    @Transactional
    public ProcessUnityDto update(ProcessUnityDto processUnityDto) {
        log.info("update : methode invocation");

        //ProcessUnity processUnity = processUnityRepository.findById(processUnityDto.getId()).orElse(null);

        Group group = camundaService.getGroup(processUnityDto.getCode());

        if (group == null) {
            throw new NotFoundException("process unity not found!");
        }

        camundaService.updateGroup(group.getId(), processUnityDto.getName(),"");
        camundaService.updateGroupMembers(group.getId(), List.of(processUnityDto.getStaffList().split(";")));


        return processUnityDto;
    }

    @Override
    public ProcessUnityDto getDetail(Long id) {
        log.info("getDetail : methode invocation");

        //ProcessUnity unity = processUnityRepository.getOne(id);

        ProcessUnityDto unityDto = new ProcessUnityDto();

        return unityDto;
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
    public List<ProcessUnityDto> getAll() {
        log.info("getAll : methode invocation");

        List<Group> groups =   camundaService.getAllGroup();

        //List<ProcessUnity> unities = processUnityRepository.findAll();

        return this.getConvertedResult(groups, "getAll");
    }

    private List<ProcessUnityDto> getConvertedResult(List<Group> groups, String functionName) {
        if (groups == null || groups.isEmpty()) {
            return new ArrayList<>();
        }
        List<ProcessUnityDto> result = new ArrayList<>();
        for (Group group : groups) {
            ProcessUnityDto processUnityDto = new ProcessUnityDto();
            processUnityDto.setCode(group.getId());
            processUnityDto.setName(group.getName());

            List<User> members = camundaService.getGroupDetailsWithMembers(group.getId());

            log.info("Membres : {}", members.size());

            String staffList = "";
            for(User m : members){
                staffList =  m.getEmail().replace("@cca-bank.com", "") + ";" + staffList;
            }
            processUnityDto.setStaffList(staffList);
            result.add(processUnityDto);
        }
        return result;
    }



}
