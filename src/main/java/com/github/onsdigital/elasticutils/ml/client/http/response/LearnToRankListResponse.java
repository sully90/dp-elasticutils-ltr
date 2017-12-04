package com.github.onsdigital.elasticutils.ml.client.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;

import java.io.IOException;
import java.util.Map;

/**
 * @author sullid (David Sullivan) on 30/11/2017
 * @project dp-elasticutils-ltr
 */
public class LearnToRankListResponse {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private float took;
    @JsonProperty("timed_out")
    private boolean timedOut;
    @JsonProperty("_shards")
    private Map<String, Object> shards;
    private LearnToRankHits hits;

    public static LearnToRankListResponse fromResponse(Response response) throws IOException {
        String json = EntityUtils.toString(response.getEntity());
        LearnToRankListResponse learnToRankResponse = MAPPER.readValue(json, LearnToRankListResponse.class);
        return learnToRankResponse;
    }

    protected LearnToRankListResponse() {

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
