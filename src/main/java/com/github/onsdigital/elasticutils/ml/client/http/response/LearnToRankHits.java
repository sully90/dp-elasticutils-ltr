package com.github.onsdigital.elasticutils.ml.client.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author sullid (David Sullivan) on 30/11/2017
 * @project dp-elasticutils-ltr
 */
public class LearnToRankHits {

    private int total;
    @JsonProperty("max_score")
    private float maxScore;
    private List<LearnToRankHit> hits;

    private LearnToRankHits() {

    }

    public int getTotal() {
        return total;
    }

    public float getMaxScore() {
        return maxScore;
    }

    public List<LearnToRankHit> getHits() {
        return hits;
    }
}
