package com.github.onsdigital.elasticutils.ml.client.response.sltr.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public abstract class SltrDocument {

    private Fields fields;
    @JsonProperty("matched_queries")
    private List<String> matchedQueries;

    public Fields getFields() {
        return fields;
    }

    public List<String> getMatchedQueries() {
        return matchedQueries;
    }

}
