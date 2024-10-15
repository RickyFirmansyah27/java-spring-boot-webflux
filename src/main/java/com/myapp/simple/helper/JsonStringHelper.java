package com.myapp.simple.helper;

public class JsonStringHelper {
    public static String toString(String columName, Object... fieldValuePairs) {
        StringBuilder result = new StringBuilder(columName + "{");
        
        for (int i = 0; i < fieldValuePairs.length; i += 2) {
            if (i + 1 < fieldValuePairs.length) {
                String fieldName = fieldValuePairs[i].toString();
                Object valueField = fieldValuePairs[i + 1];
                
                result.append(fieldName).append("=").append(valueField).append(", ");
            }
        }
    
        if (result.length() > 2) {
            result.setLength(result.length() - 2);
        }
        
        result.append("}");
        return result.toString();
    }    
}
