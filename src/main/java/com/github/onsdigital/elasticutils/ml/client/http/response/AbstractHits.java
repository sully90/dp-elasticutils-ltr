package com.github.onsdigital.elasticutils.ml.client.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public abstract class AbstractHits {

    private int total;
    @JsonProperty("max_score")
    private float maxScore;

    public int getTotal() {
        return total;
    }

    public float getMaxScore() {
        return maxScore;
    }
}
