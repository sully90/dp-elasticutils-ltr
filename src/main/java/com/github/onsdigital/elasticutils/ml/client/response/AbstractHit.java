package com.github.onsdigital.elasticutils.ml.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public abstract class AbstractHit {

    @JsonProperty("_index")
    private String index;
    @JsonProperty("_type")
    private String type;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("_score")
    private float score;

    public String getIndex() {
        return index;
    }

    @JsonProperty("_type")
    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public float getScore() {
        return score;
    }
}
