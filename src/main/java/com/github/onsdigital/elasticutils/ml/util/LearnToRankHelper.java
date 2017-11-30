package com.github.onsdigital.elasticutils.ml.util;

import com.github.onsdigital.elasticutils.ml.client.http.LeanToRankClient;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sullid (David Sullivan) on 30/11/2017
 * @project dp-elasticutils-ltr
 */
public class LearnToRankHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(LearnToRankHelper.class);

    public static final int DEFAULT_HTTP_PORT = 9200;

    public static LeanToRankClient getLTRClient(String hostName) {
        return getLTRClient(hostName, DEFAULT_HTTP_PORT);
    }

    public static LeanToRankClient getLTRClient(String hostName, int http_port) {
        LOGGER.info("Attempting to make HTTP connection to ES database: {} {}", hostName, http_port);

        // Set some basic headers for all requests
        BasicHeader[] headers = { new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json") };

        RestClientBuilder builder = RestClient.builder(new HttpHost(hostName, http_port))
                .setDefaultHeaders(headers);

        LeanToRankClient client = new LeanToRankClient(builder);

        LOGGER.info("Successfully made HTTP connection to ES database: {} {}", hostName, http_port);
        return client;
    }

}
