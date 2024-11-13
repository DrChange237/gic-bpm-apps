package com.ccabank.memoservice.util.camunda;

import com.ccabank.memoservice.dto.memo.ApprovalDto;
import com.ccabank.memoservice.dto.memo.ChoiceDto;
import com.ccabank.memoservice.dto.memo.DocumentStructure;
import com.ccabank.memoservice.dto.memo.FieldDto;
import com.ccabank.memoservice.entity.Approval;
import com.ccabank.memoservice.entity.Field;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.StartFormData;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Mapping {

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
            field.setDefaultValue(String.valueOf(f.getDefaultValue()));
            outFields.add(field);
        }

        List<ApprovalDto> outApprovals = new ArrayList<>();

        for (FormField f : approvals ) {
            ApprovalDto approval = new ApprovalDto();
            approval.setPosition(approvals.indexOf(f) + 1);
            approval.setRole(f.getLabel());
            approval.setRequired(f.getProperties().get("required").equals("true"));
            outApprovals.add(approval);
        }

        structure.setFields(outFields);
        structure.setApprovals(outApprovals);


        return structure;

    }

}
