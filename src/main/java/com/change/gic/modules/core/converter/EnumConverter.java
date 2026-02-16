package com.change.gic.modules.core.converter;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class EnumConverter<T extends Enum<T>> implements AttributeConverter<T, String> {
    private final Class<T> clazz;

    public EnumConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String convertToDatabaseColumn(T enumeration) {
        return enumeration == null ? "" : enumeration.name();
    }

    @Override
    public T convertToEntityAttribute(String s) {
        try {
            return T.valueOf(clazz, s);
        } catch (Exception e) {
            return null;
        }
    }
}
