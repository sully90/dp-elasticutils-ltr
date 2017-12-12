package com.github.onsdigital.elasticutils.ml.client.response.features;

import com.github.onsdigital.elasticutils.ml.client.response.AbstractHits;

import java.util.List;

/**
 * @author sullid (David Sullivan) on 30/11/2017
 * @project dp-elasticutils-ltr
 */
public class LearnToRankHits<T> extends AbstractHits {

    private List<LearnToRankHit<T>> hits;

    private LearnToRankHits() {

    }

    public List<LearnToRankHit<T>> getHits() {
        return hits;
    }
}
