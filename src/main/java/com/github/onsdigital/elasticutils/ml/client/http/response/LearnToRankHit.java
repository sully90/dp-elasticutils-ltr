package com.github.onsdigital.elasticutils.ml.client.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.onsdigital.elasticutils.ml.features.FeatureSet;
import com.github.onsdigital.elasticutils.ml.requests.FeatureSetRequest;

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

    public FeatureSet getFeatureSet() {
        return source.getFeatureSet();
    }
}
