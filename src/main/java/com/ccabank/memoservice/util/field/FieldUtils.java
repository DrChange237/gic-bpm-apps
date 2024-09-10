package com.ccabank.memoservice.util.field;

import com.ccabank.memoservice.dto.memo.ApprovalDto;
import com.ccabank.memoservice.dto.memo.ChoiceDto;
import com.ccabank.memoservice.dto.memo.DocumentStructure;
import com.ccabank.memoservice.dto.memo.FieldDto;
import com.ccabank.memoservice.entity.Field;
import com.ccabank.memoservice.entity.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class FieldUtils {


    public static List<FieldDto> getFieldsOfTypeAndPosition(String type, int position){

        DocumentStructure structure = getStructure(type);
        List<ApprovalDto> approvalDtos = structure.getApprovals();

        ApprovalDto approvalDto = approvalDtos.stream().filter(obj -> obj.getPosition() == position).findFirst().get();

        if(approvalDto == null){
            return null;
        }

        return approvalDto.getFields();
    }

    public static DocumentStructure getStructure(String type) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClassPathResource resource = new ClassPathResource("documents/" + type + ".json");
            InputStream inputStream = resource.getInputStream();
            DocumentStructure structure = objectMapper.readValue(inputStream, new TypeReference<DocumentStructure>() {});
            return structure;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static  String getValueOfField(Request request, String key){

        System.out.println("Get " + key);


        Collection<Field> fields = request.getFields();
        Optional<Field> field = fields.stream().filter(obj -> obj.getKey().equals(key)).findFirst();

        if(field.isEmpty()){
            System.out.println("isEmpty : " + key);
            return null;
        }

        return field.get().getValue();
    }

    public static List<ChoiceDto> getChoicesOfField(String type, String key){

        System.out.println(type);

        System.out.println(key);


        DocumentStructure structure = getStructure(type);
        List<FieldDto> fieldDtos = structure.getFields();

        Optional<FieldDto> fieldDto = fieldDtos.stream().filter(obj -> obj.getKey().equals(key)).findFirst();

        if(fieldDto.isEmpty()){
            return null;
        }

        System.out.println("Name : " +  fieldDto.get().getChoices());


        return fieldDto.get().getChoices();
    }

    public static  String getNameOfField(String type, String key){

        System.out.println(type);

        System.out.println(key);


        DocumentStructure structure = getStructure(type);
        List<FieldDto> fieldDtos = structure.getFields();

        Optional<FieldDto> fieldDto = fieldDtos.stream().filter(obj -> obj.getKey().equals(key)).findFirst();

        if(fieldDto.isEmpty()){
            return null;
        }

        System.out.println("Name : " +  fieldDto.get().getName());


        return fieldDto.get().getName();
    }

}
