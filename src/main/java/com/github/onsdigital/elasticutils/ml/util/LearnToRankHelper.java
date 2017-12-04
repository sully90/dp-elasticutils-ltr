package com.github.onsdigital.elasticutils.ml.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.onsdigital.elasticutils.ml.client.http.LearnToRankClient;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @author sullid (David Sullivan) on 30/11/2017
 * @project dp-elasticutils-ltr
 */
public class LearnToRankHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(LearnToRankHelper.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static final int DEFAULT_HTTP_PORT = 9200;

    public static LearnToRankClient getLTRClient(String hostName) {
        return getLTRClient(hostName, DEFAULT_HTTP_PORT);
    }

    public static LearnToRankClient getLTRClient(String hostName, int http_port) {
        LOGGER.info("Attempting to make HTTP connection to ES database: {} {}", hostName, http_port);

        // Set some basic headers for all requests
        BasicHeader[] headers = { new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json") };

        RestClientBuilder builder = RestClient.builder(new HttpHost(hostName, http_port))
                .setDefaultHeaders(headers);

        LearnToRankClient client = new LearnToRankClient(builder);

        LOGGER.info("Successfully made HTTP connection to ES database: {} {}", hostName, http_port);
        return client;
    }

    public static Map<String, Object> templateMapFromQueryBuilder(QueryBuilder qb) throws IOException {
        String json = qb.toString();
        Map<String, Object> templateMap = MAPPER.readValue(json, new TypeReference<Map<String, Object>>(){});
        return templateMap;
    }

}
