package com.ccabank.paperless.util.camunda;

import com.ccabank.paperless.constant.FieldTypeConstant;
import com.ccabank.paperless.dto.memo.*;
import com.ccabank.paperless.entity.ApprovalType;
import com.ccabank.paperless.exception.BadRequestException;
import com.ccabank.paperless.openfeign.FileRestClient;
import com.ccabank.paperless.util.file.FileUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.form.FormData;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.StartFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@lombok.extern.slf4j.Slf4j
@Component
@RequiredArgsConstructor
@Slf4j
public class Mapping {

    private final FileRestClient fileRestClient;

    @Value("${server_url}")
    private String serverUrl;

    private final String pathFile = "/api/files/";


    public static  List<FieldDto> getFieldFromFormField(FormData data, Map<String, Object> variables){

        List<FieldDto> fieldDtos = new ArrayList<>();

        List<FormField> fieldDatas = data.getFormFields().stream().filter(field -> field.getProperties().get("fieldType").equals("field")).collect(Collectors.toList());


        for (FormField field : fieldDatas) {
            if(field.getProperties().isEmpty()){
                continue;
            }
            FieldDto fieldDto = new FieldDto();
            fieldDto.setPosition(fieldDtos.indexOf(fieldDto));
            fieldDto.setName(field.getLabel());
            fieldDto.setKey(field.getId());
            fieldDto.setType(field.getProperties().get("type"));
            if(variables.get(field.getId()) != null){
                fieldDto.setValue(String.valueOf(variables.get(field.getId())));
            }
            fieldDto.setRequired(field.getProperties().get("required").equals("true"));


            if(fieldDto.getType().equals("choice")){
                ObjectMapper objectMapper = new ObjectMapper();
                List<ChoiceDto> choices = new ArrayList<>();
                try {
                    choices = objectMapper.readValue(field.getProperties().get("choices"),  new TypeReference<List<ChoiceDto>>() {});
                } catch (JsonProcessingException e) {
                    //throw new RuntimeException(e);
                    log.error(e.getMessage());
                    continue;
                } catch (Exception e){
                    log.error(e.getMessage());
                    continue;
                }
                fieldDto.setChoices(choices);
            }
            fieldDtos.add(fieldDto);
        }
        return fieldDtos;
    }


    public Map<String, Object> getVariablesFromField(List<FieldDto> fields)  {

        Map<String, Object> variables = new HashMap<>();

        for (FieldDto field : fields) {

            if(field.isRequired()){
                if(field.getValue() == null){
                    throw new BadRequestException("Parameter " + field.getName() + " is required");
                }
            }

            switch (field.getType()){
                case FieldTypeConstant.FILE:

                    for(FileDto file : field.getFiles()) {
                      FileDto fileFinal = fileRestClient.uploadFileToFolder("paperless", "paperless", FileUtils.convertBase64ToMultipartFile(file.getFile(), file.getName(), file.getType()));
                      variables.put(field.getKey(), serverUrl + pathFile + fileFinal.getUrl());
                    }
                    break;

                case FieldTypeConstant.DATE:
                    try {
                        if(field.getValue() != null){
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date date = formatter.parse(field.getValue());
                                variables.put(field.getKey(), date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (Exception e){
                        throw new BadRequestException("Format de la date invalid : " + field.getValue() );
                    }
                    break;

                case FieldTypeConstant.NUMBER:
                    try{
                        variables.put(field.getKey(), Long.valueOf(field.getValue()));
                    }catch (Exception e){
                        variables.put(field.getKey(), null);
                    }
                    break;

                default:
                    variables.put(field.getKey(), field.getValue());
            }
        }
        return variables;
    }


    public static Map<String, Object> getVariablesFromApproval(List<ApprovalDto> approvals) {

        Map<String, Object> variables = new HashMap<>();

        for (ApprovalDto approval : approvals) {


            if(approval.getMultiple()){
                variables.put(approval.getKey(), approval.getListStaff());
            }else {
                variables.put(approval.getKey(), approval.getStaff());
            }

        }

        return variables;
    }

    public static DocumentStructure getStructureFromFormData(StartFormData formData) {

        DocumentStructure structure = new DocumentStructure();


        structure.setName(formData.getProcessDefinition().getName());

        List<FormField> formFields =  formData.getFormFields();

        List<FormField> fields = formFields.stream().filter(field -> field.getProperties().get("fieldType").equals("field")).collect(Collectors.toList());


        List<FormField> approvals = formFields.stream().filter(field -> field.getProperties().get("fieldType").equals("approval")).collect(Collectors.toList());


        List<FieldDto> outFields = new ArrayList<>();

        for (FormField f : fields ) {
            FieldDto field = new FieldDto();
            field.setKey(f.getId());
            field.setType(f.getProperties().get("type"));
            try{
                field.setDescription(f.getProperties().get("description"));
                field.setNgIf(f.getProperties().get("ngIf"));
                if(field.getNgIf() ==  null){
                    field.setNgIf("true");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            if(field.getType().equals("choice")){
                ObjectMapper objectMapper = new ObjectMapper();
                List<ChoiceDto> choices = new ArrayList<>();
                try {
                    choices = objectMapper.readValue(f.getProperties().get("choices"),  new TypeReference<List<ChoiceDto>>() {});
                    field.setChoices(choices);
                } catch (Exception e) {
                    //throw new RuntimeException(e);
                }
            }

            field.setPosition(fields.indexOf(f) + 1);
            field.setName(f.getLabel());
            field.setRequired(f.getProperties().get("required").equals("true"));

            if(f.getDefaultValue() != null){
                field.setValue(String.valueOf(f.getDefaultValue()));
                field.setDefaultValue(String.valueOf(f.getDefaultValue()));
            }
            outFields.add(field);
        }

        List<ApprovalDto> outApprovals = new ArrayList<>();

        for (FormField f : approvals ) {
            ApprovalDto approval = new ApprovalDto();
            approval.setPosition(approvals.indexOf(f) + 1);
            approval.setRole(f.getLabel());
            approval.setType(ApprovalType.OPEN);
            approval.setKey(f.getId());
            if(f.getProperties().get("multiple") != null){
                approval.setMultiple(f.getProperties().get("multiple").equals("true"));
            }
            approval.setDescription(f.getProperties().get("description"));
            approval.setRequired(f.getProperties().get("required").equals("true"));
            outApprovals.add(approval);
        }

        structure.setFields(outFields);
        structure.setApprovals(outApprovals);


        return structure;

    }

}
