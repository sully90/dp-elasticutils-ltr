package com.github.onsdigital.elasticutils.ml.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.onsdigital.elasticutils.ml.requests.models.LogSpecs;
import com.github.onsdigital.elasticutils.ml.requests.models.SltrQuery;
import org.elasticsearch.index.query.QueryBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class LoggingQuery {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private QueryBuilder boolQuery;
    private SltrQuery sltrQuery;
    private LogSpecs logSpecs;

    public LoggingQuery(QueryBuilder filterQuery, SltrQuery sltrQuery, LogSpecs logSpecs) {
        this.boolQuery = filterQuery;
        this.sltrQuery = sltrQuery;
        this.logSpecs = logSpecs;
    }

    /**
     *
     * @return Map representation of the Elasticsearch query
     * @throws IOException
     */
    @JsonIgnore
    private Map<String, Map<String, Object>> getFilterQueryList() throws IOException {
        String queryJson = this.boolQuery.toString();
        Map<String, Map<String, Object>> queryMap = this.MAPPER.readValue(queryJson, new TypeReference<Map<String, Map<String, Object>>>(){});
        return queryMap;
    }

    /**
     *
     * @return JSON representation of a LoggingQuery, used to extract feature scores out of Learn to Rank
     * @throws IOException
     */
    public String toJson() throws IOException {
        // First, convert the regular elasticsearch query (with filter) to a Map
        Map<String, Map<String, Object>> query = this.getFilterQueryList();

        // Make sure there is a filter, as we will need to inject our sltr query in here
        if (!query.get(this.boolQuery.getName()).containsKey("filter")) {
            throw new UnsupportedOperationException("Must supply a filter query");
        } else {
            Object filterQuery = query.get(this.boolQuery.getName()).get("filter");
            // Filter queries should always be represented as a list, but perform a sanity check to make sure
            if (filterQuery instanceof List) {
                List<Object> filters = (List<Object>) filterQuery;
                // Add the sltr query
                filters.add(sltrQuery.toQueryMap());
                // Replace the filter query with our new one
                query.get(this.boolQuery.getName()).replace("filter", filters);
            } else {
                throw new UnsupportedOperationException("Filter query must be instanceof List");
            }
        }

        // Finally, add a logging extension to the query
        Map<String, Map<String, Object>> ltrLogQuery = new LinkedHashMap<>();
        ltrLogQuery.put("ltr_log", new LinkedHashMap<String, Object>() {{
            put("log_specs", logSpecs);
        }});

        // Combine all of the above together to produce the correct data structure
        Map<String, Object> finalQuery = new LinkedHashMap<>();

        finalQuery.put("query", query);
        finalQuery.put("ext", ltrLogQuery);

        // Convert to JSON
        return MAPPER.writeValueAsString(finalQuery);
    }

}
