package com.github.onsdigital.elasticutils.ml.client.http.response.features;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.onsdigital.elasticutils.ml.client.http.response.AbstractHit;
import com.github.onsdigital.elasticutils.ml.client.http.response.features.models.FeatureSet;
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

    public FeatureSet getFeatureSet() {
        return source.getFeatureSet();
    }
}
