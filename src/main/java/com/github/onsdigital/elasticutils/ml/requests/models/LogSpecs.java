package com.github.onsdigital.elasticutils.ml.requests.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.onsdigital.elasticutils.ml.query.SltrQueryBuilder;
import com.github.onsdigital.elasticutils.ml.util.JsonSerializable;
import com.github.onsdigital.elasticutils.ml.util.JsonUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class LogSpecs implements JsonSerializable {

    private String name;
    @JsonProperty("named_query")
    private String namedQuery;

    public LogSpecs(String name, String namedQuery) {
        this.name = name;
        this.namedQuery = namedQuery;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("named_query")
    public String getNamedQuery() {
        return namedQuery;
    }

    @Override
    public String toJson() throws JsonProcessingException {
        Map<String, Object> ltrLog = new LinkedHashMap<>();

        Map<String, LogSpecs> logSpecs = new LinkedHashMap<>();
        logSpecs.put("log_specs", this);
        ltrLog.put("ltr_log", logSpecs);
        return JsonUtils.toJson(ltrLog, true);
    }

    public static LogSpecs fromQuery(String name, SltrQueryBuilder queryBuilder) {
        return new LogSpecs(name, queryBuilder.getSltrQueryName());
    }
}
