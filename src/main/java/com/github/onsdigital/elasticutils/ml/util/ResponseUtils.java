package com.github.onsdigital.elasticutils.ml.util;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;

import java.io.IOException;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class ResponseUtils<T> {

    public T fromResponse(Response response, Class<T> returnClass) throws IOException {
        String json = EntityUtils.toString(response.getEntity());
        return JsonUtils.MAPPER.readValue(json, returnClass);
    }

    public T fromResponse(Response response, TypeReference typeReference) throws IOException {
        String json = EntityUtils.toString(response.getEntity());
        return JsonUtils.MAPPER.readValue(json, typeReference);
    }

}
