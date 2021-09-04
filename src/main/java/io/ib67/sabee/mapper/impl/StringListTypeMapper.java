package io.ib67.sabee.mapper.impl;

import com.google.auto.service.AutoService;
import io.ib67.sabee.mapper.ITypeMapper;
import net.jodah.typetools.TypeResolver;

import java.sql.JDBCType;
import java.sql.SQLType;
import java.util.List;

@AutoService(ITypeMapper.class)
public class StringListTypeMapper implements ITypeMapper {
    @Override
    public SQLType from(Class<?> type) {
        if(List.class.isAssignableFrom(type)){
            Class<?> args = TypeResolver.resolveRawArgument(List.class,type);
            if (args != String.class) {
                return null;
            }else{
                return JDBCType.VARCHAR;
            }
        }
        return null;
    }
}
