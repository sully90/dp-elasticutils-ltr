package com.github.onsdigital.elasticutils.ml.requests.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class SltrQuery {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @JsonProperty("_name")
    private String name;
    private String featureset;
    private Map<String, Object> params;

    public SltrQuery(String name, String featureset) {
        this.params = new LinkedHashMap<>();
        this.name = name;
        this.featureset = featureset;
    }

    @JsonProperty("_name")
    public String getName() {
        return name;
    }

    public String getFeatureset() {
        return featureset;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParam(String key, Object value) {
        this.params.put(key, value);
    }

    @JsonIgnore
    public Map<String, Object> toQueryMap() throws IOException {
        String json = this.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        Map<String, Object> params = this.MAPPER.readValue(json, new TypeReference<Map<String, Object>>(){});

        Map<String, Object> queryMap = new LinkedHashMap<>();
        queryMap.put("sltr", params);
        return queryMap;
    }

    @Override
    public String toString() {
        try {
            return this.MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
