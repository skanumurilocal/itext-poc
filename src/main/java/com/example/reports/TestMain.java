package com.example.reports;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMain {

    public static void main(String[] args) throws IOException {
        String jsonStr = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";

        ObjectMapper mapper = new ObjectMapper();

        // convert JSON file to map
        Map<String, Object> map = mapper.readValue(Paths.get("C:\\Users\\sreenuvasuk\\Downloads\\fwd14textprudhvi\\4- Text - Prudhvi.txt").toFile(), Map.class);

        // print map entries
        Map<String,String> resultMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            desensitizationMapSubProcessor(map,entry.getKey(),entry.getValue(),resultMap);
        }
        System.out.println(resultMap.toString());
    }

    private static void desensitizationMapSubProcessor(Map<String, Object> map, String key, Object value,Map<String,String> resultMap) {
        if (value == null) {
            System.out.println("map key : {}, value : null"+ key);
            return;
        }
        if (value instanceof String || value instanceof Integer
                || value instanceof Boolean || value instanceof Double) {
            if(resultMap.containsKey(key)){
                StringBuilder newString= new StringBuilder(resultMap.get(key));
                newString.append(",").append(value);
                resultMap.put(key,newString.toString());
            }else{
                resultMap.put(key,value.toString());
            }
        } else if (value instanceof List) {
            List list = (List) value;
            for (Object object : list) {
                desensitizationMapSubProcessor(map, key, object,resultMap);
            }
        } else if (value instanceof Map) {
            try {
                //noinspection unchecked
                Map<String, Object> subMap = (Map<String, Object>) value;
                desensitizationMap(subMap,resultMap);
            } catch (ClassCastException e) {
                System.out.println("cast map failed"+ e);
            }
        } else {
            throw new IllegalArgumentException(String.valueOf(value));
        }
    }

    public static void desensitizationMap(Map<String, Object> map,Map<String, String> resultMap) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            desensitizationMapSubProcessor(map, key, value,resultMap);
        }
    }
}
