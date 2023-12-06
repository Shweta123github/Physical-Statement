package com.axisbank.request.statement.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ApplicationUtils {

    private ApplicationUtils() {
    }
    public static String convertToStringOrJson(Object val, ObjectMapper mapper) {
        if (isConvertToString(val.getClass())) {
            return String.valueOf(val);
        }
        try {
            return mapper.writeValueAsString(val);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean isConvertToString(Class<?> clazz) {
        return clazz.equals(String.class) || ClassUtil.isObjectOrPrimitive(clazz);
    }
    public static void deleteLastPipe(StringBuilder parameters) {
        if (parameters != null && parameters.length() > 0)
            parameters.deleteCharAt(parameters.length() - 1);
    }

}
