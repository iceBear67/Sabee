package io.ib67.sabee.mapper;

import java.sql.SQLType;

@FunctionalInterface
public interface ITypeMapper {
    SQLType from(Class<?> type);
}
