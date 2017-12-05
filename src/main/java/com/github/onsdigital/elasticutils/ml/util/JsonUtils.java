package com.github.onsdigital.elasticutils.ml.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class JsonUtils {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static String toJson(Object object) throws JsonProcessingException {
        return toJson(object, false);
    }

    public static String toJson(Object object, boolean prettyPrint) throws JsonProcessingException {
        if (prettyPrint) {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } else {
            return MAPPER.writeValueAsString(object);
        }
    }

}
