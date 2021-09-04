package io.ib67.sabee.table.processor;

public interface IProcessor {
    Object fromDatabase(String fieldName,Object data);
    Object toDatabase(String fieldName,Object data);
}
