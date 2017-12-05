package com.github.onsdigital.elasticutils.ml.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public abstract class AbstractResponse {

    private float took;
    @JsonProperty("timed_out")
    private boolean timedOut;
    @JsonProperty("_shards")
    private Map<String, Object> shards;

    public float getTook() {
        return took;
    }

    public boolean isTimedOut() {
        return timedOut;
    }

    public Map<String, Object> getShards() {
        return shards;
    }
}
