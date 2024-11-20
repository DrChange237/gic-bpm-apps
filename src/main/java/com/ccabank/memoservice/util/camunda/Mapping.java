package com.ccabank.memoservice.util.camunda;

import com.ccabank.memoservice.constant.FieldTypeConstant;
import com.ccabank.memoservice.dto.memo.*;
import com.ccabank.memoservice.entity.ApprovalType;
import com.ccabank.memoservice.openfeign.FileRestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.form.FormData;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.StartFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Mapping {


    @Autowired
    private FileRestClient fileRestClient;


    public static  List<FieldDto> getFieldFromFormField(FormData data, Map<String, Object> variables){

        List<FieldDto> fieldDtos = new ArrayList<>();

        List<FormField> fieldDatas = data.getFormFields().stream().filter(field -> field.getProperties().get("fieldType").equals("field")).collect(Collectors.toList());


        for (FormField field : fieldDatas) {
            if(field.getProperties().isEmpty()){
                System.out.println("properties is empty");
                continue;
            }
            FieldDto fieldDto = new FieldDto();
            fieldDto.setPosition(fieldDtos.indexOf(fieldDto));
            fieldDto.setName(field.getLabel());
            fieldDto.setType(field.getProperties().get("type"));
            fieldDto.setValue(String.valueOf(variables.get(field.getId())));
            fieldDto.setRequired(field.getProperties().get("required").equals("true"));
            fieldDtos.add(fieldDto);
        }

        return fieldDtos;
    }


    public Map<String, Object> getVariablesFromField(List<FieldDto> fields) {

        Map<String, Object> variables = new HashMap<>();

        for (FieldDto field : fields) {
            switch (field.getType()){
                case FieldTypeConstant.FILE:

                    for(FileDto file : field.getFiles()) {
                    /*file = fileRestClient.uploadFileToFolder("paperless", "paperless", file.getFile());
                    variables.put(file.getName(), file.getUrl());*/
                    }
                    break;

                case FieldTypeConstant.DATE:
                    variables.put(field.getKey(), LocalDate.parse(field.getValue()));
                    break;

                    case FieldTypeConstant.NUMBER:
                        variables.put(field.getKey(), Long.valueOf(field.getValue()));
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
            variables.put(approval.getKey(), approval.getStaff());
        }

        return variables;
    }

    public static DocumentStructure getStructureFromFormData(StartFormData formData) {

        DocumentStructure structure = new DocumentStructure();


        structure.setName(formData.getProcessDefinition().getName());

        List<FormField> formFields =  formData.getFormFields();

        List<FormField> fields = formFields.stream().filter(field -> field.getProperties().get("fieldType").equals("field")).collect(Collectors.toList());

        System.out.println("Size of Fields"  + fields.size());

        List<FormField> approvals = formFields.stream().filter(field -> field.getProperties().get("fieldType").equals("approval")).collect(Collectors.toList());

        System.out.println("Size of Approvals"  + approvals.size());

        List<FieldDto> outFields = new ArrayList<>();

        for (FormField f : fields ) {
            FieldDto field = new FieldDto();
            field.setKey(f.getId());
            field.setType(f.getProperties().get("type"));
            if(field.getType().equals("choice")){
                ObjectMapper objectMapper = new ObjectMapper();
                List<ChoiceDto> choices = new ArrayList<>();
                try {
                    choices = objectMapper.readValue(f.getProperties().get("choices"),  new TypeReference<List<ChoiceDto>>() {});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                field.setChoices(choices);
            }
            field.setPosition(fields.indexOf(f) + 1);
            field.setName(f.getLabel());
            field.setRequired(f.getProperties().get("required").equals("true"));
            field.setValue(String.valueOf(f.getDefaultValue()));
            field.setDefaultValue(String.valueOf(f.getDefaultValue()));
            outFields.add(field);
        }

        List<ApprovalDto> outApprovals = new ArrayList<>();

        for (FormField f : approvals ) {
            ApprovalDto approval = new ApprovalDto();
            approval.setPosition(approvals.indexOf(f) + 1);
            approval.setRole(f.getLabel());
            approval.setType(ApprovalType.OPEN);
            approval.setKey(f.getId());
            approval.setRequired(f.getProperties().get("required").equals("true"));
            outApprovals.add(approval);
        }

        structure.setFields(outFields);
        structure.setApprovals(outApprovals);


        return structure;

    }

}
