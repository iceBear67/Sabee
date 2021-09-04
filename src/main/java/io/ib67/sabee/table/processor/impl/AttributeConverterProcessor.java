package io.ib67.sabee.table.processor.impl;

import io.ib67.sabee.table.processor.IProcessor;
import lombok.RequiredArgsConstructor;

import javax.persistence.AttributeConverter;
import java.lang.reflect.Type;

@RequiredArgsConstructor
public class AttributeConverterProcessor<X,Y> implements IProcessor {
    private final AttributeConverter<X,Y> converter;
    private final Class<Y> typeOfY;
    private final Class<X> typeOfX;
    @Override
    public Object fromDatabase(String fieldName, Object data) {
        return converter.convertToEntityAttribute(typeOfY.cast(data));
    }

    @Override
    public Object toDatabase(String fieldName, Object data) {
        return converter.convertToDatabaseColumn(typeOfX.cast(data));
    }
}
