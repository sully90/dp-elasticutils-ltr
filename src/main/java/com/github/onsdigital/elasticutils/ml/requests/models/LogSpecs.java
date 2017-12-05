package com.github.onsdigital.elasticutils.ml.requests.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class LogSpecs {

    private String name;
    @JsonProperty("named_query")
    private String namedQuery;

    public LogSpecs(String name, String namedQuery) {
        this.name = name;
        this.namedQuery = namedQuery;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("named_query")
    public String getNamedQuery() {
        return namedQuery;
    }
}
