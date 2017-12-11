package com.github.onsdigital.elasticutils.ml.client.response.features;

import com.github.onsdigital.elasticutils.ml.client.response.AbstractResponse;
import com.github.onsdigital.elasticutils.ml.util.ResponseUtils;
import org.elasticsearch.client.Response;

import java.io.IOException;

/**
 * @author sullid (David Sullivan) on 30/11/2017
 * @project dp-elasticutils-ltr
 */
public class LearnToRankListResponse extends AbstractResponse {

    private LearnToRankHits hits;

    protected LearnToRankListResponse() {

    }

    public LearnToRankHits getHits() {
        return hits;
    }

    public static LearnToRankListResponse fromResponse(Response response) throws IOException {
        ResponseUtils<LearnToRankListResponse> responseUtils = new ResponseUtils();
        return responseUtils.fromResponse(response, LearnToRankListResponse.class);
    }
}
