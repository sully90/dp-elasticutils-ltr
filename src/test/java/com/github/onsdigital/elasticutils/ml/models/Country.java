package com.github.onsdigital.elasticutils.ml.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class Country {

    private String name;
    @JsonProperty("iso_3166_1")
    private String isoName;

    private Country() {
        // For Jackson
    }

    public String getName() {
        return name;
    }

    public String getIsoName() {
        return isoName;
    }
}
