package com.github.onsdigital.elasticutils.ml.client.response.features;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.onsdigital.elasticutils.ml.client.response.AbstractResponse;
import com.github.onsdigital.elasticutils.ml.util.ResponseUtils;
import org.elasticsearch.client.Response;

import java.io.IOException;

/**
 * @author sullid (David Sullivan) on 30/11/2017
 * @project dp-elasticutils-ltr
 */
public class LearnToRankListResponse<T> extends AbstractResponse {

    private LearnToRankHits<T> hits;

    protected LearnToRankListResponse() {

    }

    public LearnToRankHits<T> getHits() {
        return hits;
    }

    public static LearnToRankListResponse fromResponse(Response response, TypeReference typeReference) throws IOException {
        ResponseUtils<LearnToRankListResponse> responseUtils = new ResponseUtils();
        return responseUtils.fromResponse(response, typeReference);
    }
}
