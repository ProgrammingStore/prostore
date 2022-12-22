package ru.programstore.prostore.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionUtil {
    public static Type getGenericType(Object o, int index) {
        ParameterizedType genericInterface = (ParameterizedType) o.getClass().getGenericInterfaces()[0];
        return genericInterface.getActualTypeArguments()[index];
    }
}
