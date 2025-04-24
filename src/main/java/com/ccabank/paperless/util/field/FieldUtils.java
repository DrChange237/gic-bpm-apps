package com.ccabank.paperless.util.field;

import com.ccabank.paperless.dto.memo.ChoiceDto;

import java.util.ArrayList;
import java.util.List;

public class FieldUtils {

    public static List<ChoiceDto> mapToChoiceDtoList(List<Object> objects, String labelProperty, String valueProperty) {
        List<ChoiceDto> choiceDtos = new ArrayList<>();
        for (Object object : objects) {
            ChoiceDto choice = new ChoiceDto();
            choice.setLabel("");
            choice.setValue("");
        }
        return choiceDtos;

    }

}
