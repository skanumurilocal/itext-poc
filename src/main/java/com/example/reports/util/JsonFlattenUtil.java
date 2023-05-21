package com.example.reports.util;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JsonFlattenUtil {

    public void jsonFlattenProcessor(Map<String,Object> inputMap, Map<String,String> resultMap){
        for (Map.Entry<String, Object> entry :
                inputMap.entrySet()) {
            jsonFlattenProcessor(inputMap,entry.getKey(),entry.getValue(),resultMap);
        }

    }

    public  void jsonFlattenProcessor(Map<String, Object> map, String key, Object value, Map<String,String> resultMap) {
        if (value == null) {
            System.out.println("map key : {}, value : null"+ key);
            return;
        }
        if (value instanceof String || value instanceof Integer
                || value instanceof Boolean || value instanceof Double) {
            if(resultMap.containsKey(key)){
                StringBuilder newString= new StringBuilder(resultMap.get(key));
                if(newString.indexOf(value.toString())==-1)
                newString.append(",").append(value);
                resultMap.put(key,newString.toString());
            }else{
                resultMap.put(key,value.toString());
            }
        } else if (value instanceof List) {
            List list = (List) value;
            for (Object object : list) {
                jsonFlattenProcessor(map, key, object,resultMap);
            }
        } else if (value instanceof Map) {
            try {
                //noinspection unchecked
                Map<String, Object> subMap = (Map<String, Object>) value;
                jsonFlattenSubProcessor(subMap,resultMap);
            } catch (ClassCastException e) {
                System.out.println("cast map failed"+ e);
            }
        } else {
            throw new IllegalArgumentException(String.valueOf(value));
        }
    }

    public void jsonFlattenSubProcessor(Map<String, Object> map,Map<String, String> resultMap) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            jsonFlattenProcessor(map, key, value,resultMap);
        }
    }
}
