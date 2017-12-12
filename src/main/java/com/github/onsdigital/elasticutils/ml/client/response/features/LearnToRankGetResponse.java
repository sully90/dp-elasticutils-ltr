package com.github.onsdigital.elasticutils.ml.client.response.features;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sullid (David Sullivan) on 04/12/2017
 * @project dp-elasticutils-ltr
 */
public class LearnToRankGetResponse<T> {

    @JsonProperty("_index")
    private String index;
    @JsonProperty("_type")
    private String type;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("_version")
    private String version;
    private boolean found;
    @JsonProperty("_source")
    private T source;

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public boolean isFound() {
        return found;
    }

    public T getSource() {
        return source;
    }
}
