package com.ccabank.memoservice.util.camunda;

import com.ccabank.memoservice.constant.FieldTypeConstant;
import com.ccabank.memoservice.dto.memo.*;
import com.ccabank.memoservice.entity.ApprovalType;
import com.ccabank.memoservice.openfeign.FileRestClient;
import com.ccabank.memoservice.util.file.FileUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.form.FormData;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.StartFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Mapping {


    @Autowired
    private FileRestClient fileRestClient;


    public static  List<FieldDto> getFieldFromFormField(FormData data, Map<String, Object> variables){

        List<FieldDto> fieldDtos = new ArrayList<>();

        List<FormField> fieldDatas = data.getFormFields().stream().filter(field -> field.getProperties().get("fieldType").equals("field")).collect(Collectors.toList());


        for (FormField field : fieldDatas) {
            System.out.println(field.getId());
            if(field.getProperties().isEmpty()){
                System.out.println("properties is empty");
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
                System.out.println("choice is choice");
                ObjectMapper objectMapper = new ObjectMapper();
                List<ChoiceDto> choices = new ArrayList<>();
                try {
                    choices = objectMapper.readValue(field.getProperties().get("choices"),  new TypeReference<List<ChoiceDto>>() {});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                fieldDto.setChoices(choices);
            }
            fieldDtos.add(fieldDto);
        }
        return fieldDtos;
    }


    public Map<String, Object> getVariablesFromField(List<FieldDto> fields) throws Exception {

        Map<String, Object> variables = new HashMap<>();

        for (FieldDto field : fields) {

            System.out.println("In Field : " + field.getName());


            if(field.isRequired()){
                System.out.println("Field is required : " + field.getName());
                if(field.getValue() == null){
                    throw new Exception("Parameter " + field.getName() + " is required");
                }
            }

            switch (field.getType()){
                case FieldTypeConstant.FILE:

                    for(FileDto file : field.getFiles()) {
                      file = fileRestClient.uploadFileToFolder("paperless", "paperless", FileUtils.convertBase64ToMultipartFile(file.getFile(), file.getName(), "application/octet-stream"));
                      variables.put(file.getName(), file.getUrl());
                    }
                    break;

                case FieldTypeConstant.DATE:
                    try {
                        if(field.getValue() != null){
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                            try {
                                Date date = formatter.parse(field.getValue());
                                System.out.println("Parsed Date: " + date);
                                variables.put(field.getKey(), date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (Exception e){
                        throw new Exception("Format de la date invalid : " + field.getValue() );
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
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                field.setChoices(choices);
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
            approval.setDescription(f.getProperties().get("description"));
            approval.setRequired(f.getProperties().get("required").equals("true"));
            outApprovals.add(approval);
        }

        structure.setFields(outFields);
        structure.setApprovals(outApprovals);


        return structure;

    }

}
