package com.github.onsdigital.elasticutils.ml.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.onsdigital.elasticutils.ml.requests.models.SltrQuery;
import org.elasticsearch.index.query.QueryBuilder;

import java.io.IOException;
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

    public LoggingQuery(QueryBuilder filterQuery, SltrQuery sltrQuery) {
        this.boolQuery = filterQuery;
        this.sltrQuery = sltrQuery;
    }

    @JsonIgnore
    private Map<String, Map<String, Object>> getFilterQueryList() throws IOException {
        String queryJson = this.boolQuery.toString();
        Map<String, Map<String, Object>> queryMap = this.MAPPER.readValue(queryJson, new TypeReference<Map<String, Map<String, Object>>>(){});
        return queryMap;
    }

    public String toJson() throws IOException {
        Map<String, Map<String, Object>> query = this.getFilterQueryList();

        if (!query.get(this.boolQuery.getName()).containsKey("filter")) {
            throw new UnsupportedOperationException("Must supply a filter query");
        } else {
            Object filterQuery = query.get(this.boolQuery.getName()).get("filter");
            if (filterQuery instanceof List) {
                List<Object> filters = (List<Object>) filterQuery;
                filters.add(sltrQuery.toQueryMap());
                query.get(this.boolQuery.getName()).replace("filter", filters);
            } else {
                throw new UnsupportedOperationException("Filter query must be instanceof List");
            }
        }

        return MAPPER.writeValueAsString(query);
    }

}
