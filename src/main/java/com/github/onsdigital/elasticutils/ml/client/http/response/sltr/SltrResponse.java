package com.github.onsdigital.elasticutils.ml.client.http.response.sltr;

import com.github.onsdigital.elasticutils.ml.client.http.response.AbstractResponse;
import com.github.onsdigital.elasticutils.ml.util.ResponseUtils;
import org.elasticsearch.client.Response;

import java.io.IOException;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class SltrResponse extends AbstractResponse {

    private SltrHits hits;

    private SltrResponse() {
        // For Jackson
    }

    public SltrHits getHits() {
        return hits;
    }

    public static SltrResponse fromResponse(Response response) throws IOException {
        ResponseUtils<SltrResponse> responseUtils = new ResponseUtils();
        return responseUtils.fromResponse(response, SltrResponse.class);
    }

}
