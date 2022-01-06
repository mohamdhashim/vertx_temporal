package com.example.hello;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;

public class Json {
    private static ObjectMapper objMapper = getDefaultObjectMapper();

    private static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper defaultObjMapper = new ObjectMapper();
        defaultObjMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return defaultObjMapper;
    }

    public static JsonNode parse(String s) throws IOException{
        return objMapper.readTree(s);
    }

    public static <A> A fromJson(JsonNode node, Class<A> CLASS) throws JsonProcessingException {
        return  objMapper.treeToValue(node,CLASS);
    }

    public static  JsonNode toJson(Object a){
        return objMapper.valueToTree(a);
    }


    public static  String toString(JsonNode a) throws JsonProcessingException {
        ObjectWriter objectWriter = objMapper.writer();
        objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        return objectWriter.writeValueAsString(a);
    }
}
