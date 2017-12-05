package com.github.onsdigital.elasticutils.ml.client.http.response.features;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.onsdigital.elasticutils.ml.requests.FeatureSetRequest;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;

import java.io.IOException;

/**
 * @author sullid (David Sullivan) on 04/12/2017
 * @project dp-elasticutils-ltr
 */
public class LearnToRankGetResponse {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @JsonProperty("_index")
    private String index;
    @JsonProperty("_type")
    private String type;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("_version")
    private String version;
    private boolean found;
    @JsonProperty("_source")
    private FeatureSetRequest source;

    public static LearnToRankGetResponse fromResponse(Response response) throws IOException {
        String json = EntityUtils.toString(response.getEntity());
        LearnToRankGetResponse getResponse = MAPPER.readValue(json, LearnToRankGetResponse.class);
        return getResponse;
    }

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public boolean isFound() {
        return found;
    }

    public FeatureSetRequest getSource() {
        return source;
    }
}
