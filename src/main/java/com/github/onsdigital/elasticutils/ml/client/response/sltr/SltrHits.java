package com.github.onsdigital.elasticutils.ml.client.response.sltr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.onsdigital.elasticutils.ml.client.response.AbstractHits;
import com.github.onsdigital.elasticutils.ml.client.response.sltr.models.SltrDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class SltrHits<T> extends AbstractHits {

    private List<SltrHit> hits;

    private SltrHits() {

    }

    public List<SltrHit> getHits() {
        return hits;
    }

    @JsonIgnore
    public List<T> asClass(Class<? extends SltrDocument> returnClass) throws IOException {
        List<T> returnList = new ArrayList<>();
        for (SltrHit<T> hit : this.getHits()) {
            returnList.add(hit.asClass(returnClass));
        }
        return returnList;
    }
}
