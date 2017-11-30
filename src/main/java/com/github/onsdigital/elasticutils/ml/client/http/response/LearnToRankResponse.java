package com.github.onsdigital.elasticutils.ml.client.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * @author sullid (David Sullivan) on 30/11/2017
 * @project dp-elasticutils-ltr
 */
public class LearnToRankResponse {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private float took;
    @JsonProperty("timed_out")
    private boolean timedOut;
    @JsonProperty("_shards")
    private Map<String, Object> shards;
    private LearnToRankHits hits;

    protected LearnToRankResponse() {

    }

    public float getTook() {
        return took;
    }

    @JsonProperty("timed_out")
    public boolean isTimedOut() {
        return timedOut;
    }

    @JsonProperty("_shards")
    public Map<String, Object> getShards() {
        return shards;
    }

    public LearnToRankHits getHits() {
        return hits;
    }

//    @JsonIgnore
//    public List<FeatureSet> getFeatureSets() throws IOException {
//        String json = MAPPER.writeValueAsString(this.hits.get("hits"));
//        FeatureSet[] featureSets = MAPPER.readValue(json, FeatureSet[].class);
//        return Arrays.asList(featureSets);
//    }
}
