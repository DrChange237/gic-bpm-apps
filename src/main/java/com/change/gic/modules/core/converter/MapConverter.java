package com.change.gic.modules.core.converter;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Converter
public class MapConverter implements AttributeConverter<Map<String, String>, String> {
    public MapConverter() {
    }

    public String convertToDatabaseColumn(Map<String, String> text) {
        return text != null && !text.isEmpty() ? text.entrySet().stream().map((entry) -> {
            String var10000 = entry.getKey().replace("=", "*").replace(";", "**").trim();
            return var10000 + "=" + entry.getValue().replace("=", "*").replace(";", "**").trim();
        }).collect(Collectors.joining(";")) : "";
    }

    public Map<String, String> convertToEntityAttribute(String s) {
        return (Map)(StringUtils.isBlank(s) ? new HashMap() : (Map)Arrays.stream(s.split(";")).map((entry) -> {
            return entry.split("=");
        }).filter((table) -> {
            return table.length > 1;
        }).map((table) -> {
            return Pair.of(table[0], table[1]);
        }).collect(Collectors.toMap(Pair::getKey, Pair::getValue)));
    }
}