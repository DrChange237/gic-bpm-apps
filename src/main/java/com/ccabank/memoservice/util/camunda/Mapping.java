package com.ccabank.memoservice.util.camunda;

import com.ccabank.memoservice.dto.memo.*;
import com.ccabank.memoservice.entity.ApprovalType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.StartFormData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Mapping {


    public static Map<String, Object> getVariablesFromField(List<FieldDto> fields) {

        Map<String, Object> variables = new HashMap<>();

        for (FieldDto field : fields) {
            variables.put(field.getKey(), field.getValue());
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
