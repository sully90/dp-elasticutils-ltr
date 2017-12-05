package com.github.onsdigital.elasticutils.ml.util;

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

}
