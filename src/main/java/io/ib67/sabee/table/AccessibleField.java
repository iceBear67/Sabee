package io.ib67.sabee.table;

import io.ib67.sabee.mapper.SQLTypeMapper;
import io.ib67.sabee.table.processor.IProcessor;
import io.ib67.sabee.util.Unsafe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.persistence.AttributeConverter;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.sql.JDBCType;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用于访问 Field 的包装。
 * 访问方式有两种：
 * 1. 通过暴露出来的 getter / setter
 * 2. 使用 unsafe 设置
 * @param <T>
 */
public class AccessibleField<T> {
    private Class<T> clazz;
    @Getter
    private String fieldName;
    private MethodHandle setter;
    private MethodHandle getter;
    private long offset;
    private SQLType type;
    private List<IProcessor> processors=new ArrayList<>(); //todo discover
    @SneakyThrows
    public AccessibleField(Class<T> clazz,String fieldName){
        this.clazz=clazz;
        this.fieldName = fieldName;
        /**
         * Find Setters.
         */
        Field field = clazz.getDeclaredField(fieldName);
        Class<?> fieldType = field.getType();
        try {
            setter = MethodHandles.lookup().findVirtual(clazz, "set" + uppercaseFirst(fieldName), MethodType.methodType(void.class, fieldType));
        }catch(Throwable excepted){
            // Maybe there isn't a setter method.
            offset = Unsafe.objectFieldOffset(field);
        }
        try {
            getter = MethodHandles.lookup().findVirtual(clazz, "get" + uppercaseFirst(fieldName), MethodType.methodType(fieldType));
        }catch(Throwable excepted){
            // Maybe there isn't a Getter method.
            if(offset!=0L) offset = Unsafe.objectFieldOffset(field);
        }
        type = SQLTypeMapper.from(fieldType);
    }

    @SneakyThrows
    public void set(T t,Object data){
        Object d = processors.stream().map(e->e.fromDatabase(fieldName,data)).filter(Objects::nonNull).findFirst().orElse(data);

        if(setter!=null){
            setter.invokeExact(t,d);
        }
        assert offset!=0;
        Unsafe.putObject(t,offset,d);
    }
    @SneakyThrows
    public Object get(T t){
        Object data = getter==null?Unsafe.getObject(t,offset):getter.invokeExact();
        return processors.stream().map(e->e.toDatabase(fieldName,data)).filter(Objects::nonNull).findFirst().orElse(data);
    }
    private static final String uppercaseFirst(String str){
        char[] arr = str.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }

    public SQLType asSqlType(){
        return type;
    }
}
