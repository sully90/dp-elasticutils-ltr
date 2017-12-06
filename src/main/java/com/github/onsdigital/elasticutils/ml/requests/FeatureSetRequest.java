package com.github.onsdigital.elasticutils.ml.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.onsdigital.elasticutils.ml.client.response.features.models.FeatureSet;
import com.github.onsdigital.elasticutils.ml.util.JsonSerializable;
import com.github.onsdigital.elasticutils.ml.util.JsonUtils;

import java.io.IOException;

/**
 * @author sullid (David Sullivan) on 23/11/2017
 * @project dp-elasticutils-ltr
 */
public class FeatureSetRequest implements JsonSerializable {

    private FeatureSet featureSet;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Validation validation;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;

    protected FeatureSetRequest() {
        // For Jackson
    }

    public FeatureSetRequest(FeatureSet featureSet) {
        this.featureSet = featureSet;
    }

    public FeatureSetRequest(FeatureSet featureSet, Validation validation) {
        this(featureSet);
        this.validation = validation;
    }

    @JsonIgnore
    public static FeatureSetRequestBuilder builder() {
        return new FeatureSetRequestBuilder();
    }

    @JsonProperty("featureset")
    public FeatureSet getFeatureSet() {
        return featureSet;
    }

    public void setFeatureSet(FeatureSet featureSet) {
        this.featureSet = featureSet;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Validation getValidation() {
        return validation;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    @JsonIgnore
    public String getName() {
        return this.featureSet.getName();
    }

    public String getType() {
        return type;
    }

    @Override
    public String toJson() throws IOException {
        return JsonUtils.toJson(this, true);
    }

    public static class FeatureSetRequestBuilder {
        @JsonIgnore
        private FeatureSetRequest request = new FeatureSetRequest();

        @JsonIgnore
        public FeatureSetRequestBuilder featureSet(FeatureSet featureSet) {
            this.request.setFeatureSet(featureSet);
            return this;
        }

        @JsonIgnore
        public FeatureSetRequestBuilder validation(Validation validation) {
            this.request.setValidation(validation);
            return this;
        }

        @JsonIgnore
        public FeatureSetRequest build() {
            return this.request;
        }

    }

}
