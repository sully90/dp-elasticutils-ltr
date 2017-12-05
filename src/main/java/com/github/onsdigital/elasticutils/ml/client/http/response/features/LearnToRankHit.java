package com.github.onsdigital.elasticutils.ml.client.http.response.features;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.onsdigital.elasticutils.ml.features.FeatureSet;
import com.github.onsdigital.elasticutils.ml.requests.FeatureSetRequest;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;

import java.io.IOException;

/**
 * @author sullid (David Sullivan) on 30/11/2017
 * @project dp-elasticutils-ltr
 */
public class LearnToRankHit {

    @JsonProperty("_index")
    private String index;
    @JsonProperty("_type")
    private String type;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("_score")
    private float score;
    @JsonProperty("_source")
    private FeatureSetRequest source;

    private LearnToRankHit() {

    }

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public float getScore() {
        return score;
    }

    public FeatureSetRequest getSource() {
        return source;
    }

    public FeatureSet getFeatureSet() {
        return source.getFeatureSet();
    }
}
