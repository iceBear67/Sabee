package io.ib67.sabee.table;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 构造输出和装配表
 * @param <T>
 */
public class Table<T> implements Cloneable{
    private Class<T> classOfT;
    private Pojo<T> initializer;
    public Table(Class<T> classOfT){
        initializer = new Pojo<>(classOfT);
    }
    private String getTableName(){
        if(classOfT.isAnnotationPresent(javax.persistence.Table.class)){
            javax.persistence.Table table = classOfT.getAnnotation(javax.persistence.Table.class);
            return table.name();
        }
        return classOfT.getSimpleName();
    }
    public String generateTableSQL(){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE "+getTableName()+" (");
        for (AccessibleField<T> field : initializer.getAccessibleFields().values()) {
            sb.append(field.getFieldName()).append(" ").append(field.asSqlType().getName()).append(','); //todo
        }
    }
    @SneakyThrows
    public List<T> toResults(ResultSet rs){
        List<T> list = new ArrayList<>();
        while (rs.next()) {
            list.add(byOne(rs));
        }
        return list;
    }

    @SneakyThrows
    private Map<String,Object> toMap(ResultSet rs){
        Map<String,Object> objectMap = new HashMap<>();
        for (Field field : fields) {
            objectMap.put(field.getName(),rs.getObject(field.getName()));
        }
        return objectMap;
    }

    private T byOne(ResultSet rs){
        return initializer.init(toMap(rs));
    }

    private Table(){

    }

    @Override
    public Table<T> clone(){
        Table<T> newTable = new Table<>();
        newTable.initializer=initializer;
        newTable.classOfT=classOfT;
        return newTable;
    }
}
