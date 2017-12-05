package com.github.onsdigital.elasticutils.ml.client.response.sltr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.onsdigital.elasticutils.ml.client.response.AbstractHit;
import com.github.onsdigital.elasticutils.ml.client.response.sltr.models.Fields;
import com.github.onsdigital.elasticutils.ml.client.response.sltr.models.SltrDocument;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class SltrHit<T> extends AbstractHit {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @JsonProperty("_source")
    private Map<String, Object> source;
    private Fields fields;
    @JsonProperty("matched_queries")
    private List<String> matchedQueries;

    private SltrHit() {
        // For Jackson
    }

    public Map<String, Object> getSource() {
        return source;
    }

    public Fields getFields() {
        return fields;
    }

    public List<String> getMatchedQueries() {
        return matchedQueries;
    }

    @JsonIgnore
    public String toJson() throws JsonProcessingException {
        Map<String, Object> jsonMap = new LinkedHashMap<>();
        jsonMap.putAll(this.source);
        jsonMap.put("fields", this.fields);
        jsonMap.put("matched_queries", this.matchedQueries);

        return MAPPER.writeValueAsString(jsonMap);
    }

    @JsonIgnore
    public T asClass(Class<? extends SltrDocument> returnClass) throws IOException {
        String jsonSource = this.toJson();
        return MAPPER.readValue(jsonSource, (Class<T>) returnClass);
    }

}
