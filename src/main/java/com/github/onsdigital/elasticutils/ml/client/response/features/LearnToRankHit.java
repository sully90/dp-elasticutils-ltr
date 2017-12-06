package com.github.onsdigital.elasticutils.ml.client.response.features;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.onsdigital.elasticutils.ml.client.response.AbstractHit;
import com.github.onsdigital.elasticutils.ml.client.response.features.models.FeatureSet;
import com.github.onsdigital.elasticutils.ml.requests.FeatureSetRequest;

/**
 * @author sullid (David Sullivan) on 30/11/2017
 * @project dp-elasticutils-ltr
 */
public class LearnToRankHit extends AbstractHit {

    @JsonProperty("_source")
    private FeatureSetRequest source;

    private LearnToRankHit() {

    }

    public FeatureSetRequest getSource() {
        return source;
    }

    @JsonIgnore
    public FeatureSet getFeatureSet() {
        return source.getFeatureSet();
    }
}
