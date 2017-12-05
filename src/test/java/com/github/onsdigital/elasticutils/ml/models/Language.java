package com.github.onsdigital.elasticutils.ml.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class Language {

    @JsonProperty("iso_639_1")
    private String isoName;
    private String name;

    private Language() {
        // For Jackson
    }

    public String getIsoName() {
        return isoName;
    }

    public String getName() {
        return name;
    }
}
