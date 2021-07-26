package org.forstudy.converter;

import java.lang.reflect.InvocationTargetException;

public interface ObjectMapper<C, M> {
    <T> T convertFrom(C value, Class<T> tClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException;

    C convertTo(M c) throws NoSuchFieldException, IllegalAccessException;
}
