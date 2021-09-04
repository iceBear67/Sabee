package io.ib67.sabee.table;

import io.ib67.sabee.util.Unsafe;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class Pojo<T> {
    @Getter
    private Map<String,AccessibleField<T>> accessibleFields = new HashMap<>();
    // nullable
    private Constructor<T> constructor;
    private String[] constructorArg;
    private Class<T> classOfT;
    public Pojo(Class<T> classOfT){
        this.classOfT=classOfT;
        for (Field declaredField : classOfT.getDeclaredFields()) {
            if(Modifier.isTransient(declaredField.getModifiers())){
                continue;
            }
            accessibleFields.put(declaredField.getName(),new AccessibleField<>(classOfT,declaredField.getName()));
        }
        // empty constructor?
        try {
            constructor = classOfT.getDeclaredConstructor();
            constructor.setAccessible(true);
        }catch(Throwable excepted){
            // constructor with field arguments?
            try {
                List<Class<?>> list = new ArrayList<>();
                List<String> constArgs = new ArrayList<>();
                for (Field declaredField : classOfT.getDeclaredFields()) {
                    if(Modifier.isTransient(declaredField.getModifiers()))continue;
                    list.add(declaredField.getType());
                    constArgs.add(declaredField.getName());
                }
                constructor = classOfT.getDeclaredConstructor(list.toArray(new Class<?>[0]));
                constructor.setAccessible(true);
                constructorArg=constArgs.toArray(new String[0]);
            }catch(Throwable anotherExcepted){
                // Use unsafe.
            }
        }
    }
    public T init(Map<String,Object> data){
        if (constructorArg!=null) {
            return constructByArg(data);
        }
        T t = construct();
        for (String key : data.keySet()) {
            Object value = data.get(key);
            accessibleFields.get(key).set(t,value);
        }
        return t;
    }
    @SneakyThrows
    private T constructByArg(Map<String,Object> data){
        List<Object> args = new ArrayList<>();
        for (String s : constructorArg) {
            args.add(data.get(s)); // sort
        }
        return constructor.newInstance(args);
    }
    @SneakyThrows
    @SuppressWarnings("unchecked")
    private T construct(){
        if(constructor!=null){
            return constructor.newInstance();
        }
        return (T) Unsafe.getUnsafe().allocateInstance(classOfT);
    }
}
