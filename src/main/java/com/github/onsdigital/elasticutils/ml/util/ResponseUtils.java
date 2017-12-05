package com.github.onsdigital.elasticutils.ml.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;

import java.io.IOException;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class ResponseUtils<T> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public T fromResponse(Response response, Class<T> returnClass) throws IOException {
        String json = EntityUtils.toString(response.getEntity());
        return MAPPER.readValue(json, returnClass);
    }

}
