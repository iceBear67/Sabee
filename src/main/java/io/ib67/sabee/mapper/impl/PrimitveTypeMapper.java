package io.ib67.sabee.mapper.impl;

import com.google.auto.service.AutoService;
import io.ib67.sabee.mapper.ITypeMapper;

import java.sql.JDBCType;
import java.sql.SQLType;

@AutoService(ITypeMapper.class)
public class PrimitveTypeMapper implements ITypeMapper{
    @Override
    public SQLType from(Class<?> ctype) {
        SQLType type=null;
        switch(ctype.getName()){
            // primitve types
            case "int":
                type = JDBCType.INTEGER;
            case "double":
                type = JDBCType.DOUBLE;
            case "float":
                type = JDBCType.FLOAT;
            case "short":
                type = JDBCType.INTEGER;
            case "char":
                type = JDBCType.CHAR;
            case "byte":
                type = JDBCType.BIT;
            case "[B":
                type = JDBCType.BLOB; // byte[]
            default:
                if (ctype.getName().startsWith("[") && ctype.getName().length()==2) {
                    type = JDBCType.ARRAY;
                }
        }
        return type;
    }
}
