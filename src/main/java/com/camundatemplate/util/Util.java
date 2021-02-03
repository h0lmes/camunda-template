package com.camundatemplate.util;

import java.util.Map;

public class Util {

    public static String variablesToString(Map<String, Object> variables) {
        StringBuilder s = new StringBuilder("{");
        variables.forEach((key, val) -> {
            if (s.length() > 1)
                s.append(", ");
            s.append(key).append(" = ").append(val);
        });
        return s.append("}").toString();
    }
}
