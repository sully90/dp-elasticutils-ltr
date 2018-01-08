package com.github.onsdigital.elasticutils.ml.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.onsdigital.elasticutils.ml.query.SltrQueryBuilder;
import com.github.onsdigital.elasticutils.ml.requests.models.LogSpecs;
import com.github.onsdigital.elasticutils.ml.util.JsonSerializable;
import com.github.onsdigital.elasticutils.ml.util.JsonUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class LogQuerySearchRequest implements JsonSerializable {

    private QueryBuilder loggingQuery;
    private LogSpecs logSpecs;

    public LogQuerySearchRequest(QueryBuilder loggingQuery, LogSpecs logSpecs) {
        if (!loggingQuery.toString().contains(SltrQueryBuilder.NAME)) {
            throw new UnsupportedOperationException("Must supply a sltr query");
        }
        this.loggingQuery = loggingQuery;
        this.logSpecs = logSpecs;
    }

    /**
     *
     * @return Map representation of the Elasticsearch query
     * @throws IOException
     */
    @JsonIgnore
    private Map<String, Object> getQueryMap() throws IOException {
        return this.getQueryMap(-1);
    }

    /**
     *
     * @return Map representation of the Elasticsearch query
     * @throws IOException
     */
    @JsonIgnore
    private Map<String, Object> getQueryMap(int size) throws IOException {
        Map<String, Object> queryMap = new LinkedHashMap<>();

        if (size > 0) {
            queryMap.put("size", size);
        }

        Map<String, Object> qbMap = JsonUtils.MAPPER.readValue(this.loggingQuery.toString(), new TypeReference<Map<String, Object>>(){});
        queryMap.put("query", qbMap);

        Map<String, Object> extMap = JsonUtils.MAPPER.readValue(this.logSpecs.toJson(), new TypeReference<Map<String, Object>>(){});
        queryMap.put("ext", extMap);
        return queryMap;
    }

    /**
     *
     * @return JSON representation of a LogQuerySearchRequest, used to extract feature scores out of Learn to Rank
     * @throws IOException
     */
    @Override
    public String toJson() throws IOException {
        return this.toJson(-1);
    }

    /**
     *
     * @return JSON representation of a LogQuerySearchRequest, used to extract feature scores out of Learn to Rank
     * @throws IOException
     */
    public String toJson(int size) throws IOException {
        return JsonUtils.toJson(this.getQueryMap(size), true);
    }

    public void toFile(File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(this.toJson());
        fileWriter.flush();
    }

    public static String readJsonFromFile(File file) throws IOException {
        String json = JsonUtils.MAPPER.readValue(file, String.class);
        if (!json.contains(SltrQueryBuilder.NAME) || !json.contains("ltr_log")) {
            throw new UnsupportedOperationException("Json file does not contain either a sltr or ext block");
        }
        return json;
    }

    public static LogQuerySearchRequest getRequestForQuery(QueryBuilder queryBuilder, SltrQueryBuilder sltrQueryBuilder) {
        QueryBuilder qb = QueryBuilders.boolQuery()
                .filter(queryBuilder)
                .filter(sltrQueryBuilder);
        LogSpecs logSpecs = LogSpecs.fromQuery("log_entry", sltrQueryBuilder);
        return new LogQuerySearchRequest(qb, logSpecs);
    }
}
