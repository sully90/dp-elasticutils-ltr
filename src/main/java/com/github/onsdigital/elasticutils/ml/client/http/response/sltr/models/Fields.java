package com.github.onsdigital.elasticutils.ml.client.http.response.sltr.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class Fields {

    @JsonProperty("_ltrlog")
    private List<Map<String, List<LogEntry>>> ltrLogList;
    @JsonProperty("matched_queries")
    private List<String> matchedQueries;

    private Fields() {
        // For Jackson
    }

    public List<Map<String, List<LogEntry>>> getLtrLogList() {
        return ltrLogList;
    }

    public List<String> getMatchedQueries() {
        return matchedQueries;
    }
}
