package io.ib67.sabee.mapper;

import java.sql.JDBCType;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

public class SQLTypeMapper {
    private static List<ITypeMapper> mappers=new ArrayList<>();
    static {
        ServiceLoader.load(ITypeMapper.class).forEach(e->mappers.add(e));
    }
    public static final SQLType from(Class<?> ctype){
        return mappers.stream().map(e->e.from(ctype)).filter(Objects::nonNull).findFirst().orElse(JDBCType.VARCHAR);
    }
}
