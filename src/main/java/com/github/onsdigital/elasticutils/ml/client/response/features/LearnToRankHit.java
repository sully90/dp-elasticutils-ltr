package com.github.onsdigital.elasticutils.ml.client.response.features;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.onsdigital.elasticutils.ml.client.response.AbstractHit;

/**
 * @author sullid (David Sullivan) on 30/11/2017
 * @project dp-elasticutils-ltr
 */
public class LearnToRankHit<T> extends AbstractHit {

    @JsonProperty("_source")
    private T source;

    private LearnToRankHit() {

    }

    @JsonProperty("_source")
    public T getSource() {
        return source;
    }
}
