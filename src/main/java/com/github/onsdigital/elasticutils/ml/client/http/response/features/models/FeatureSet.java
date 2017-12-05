package com.github.onsdigital.elasticutils.ml.client.http.response.features.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sullid (David Sullivan) on 23/11/2017
 * @project dp-elasticutils-ltr
 *
 * Simple class to represent a FeatureSet in Elasticsearch LTR
 */
public class FeatureSet {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    private List<Feature> featureList;

    private FeatureSet() {
        this.featureList = new ArrayList<>();
    }

    public FeatureSet(String name) {
        this();
        this.name = name;
    }

    public FeatureSet(String name, List<Feature> featureList) {
        this.name = name;
        this.featureList = featureList;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getName() {
        return name;
    }

    @JsonProperty("features")
    public List<Feature> getFeatureList() {
        return featureList;
    }

    @JsonIgnore
    public String toJson() throws JsonProcessingException {
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof FeatureSet) {
            FeatureSet otherFeatureSet = (FeatureSet) other;
            return (otherFeatureSet.getName().equals(this.getName()));
        }
        return false;
    }
}
