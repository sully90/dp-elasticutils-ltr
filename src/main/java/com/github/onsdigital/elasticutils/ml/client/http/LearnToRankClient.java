package com.github.onsdigital.elasticutils.ml.client.http;

import com.github.onsdigital.elasticutils.ml.client.http.response.LearnToRankGetResponse;
import com.github.onsdigital.elasticutils.ml.client.http.response.LearnToRankListResponse;
import com.github.onsdigital.elasticutils.ml.requests.FeatureSetRequest;
import com.github.onsdigital.elasticutils.ml.util.LearnToRankHelper;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.common.Strings;
import org.elasticsearch.xpack.common.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author sullid (David Sullivan) on 30/11/2017
 * @project dp-elasticutils-ltr
 */
public class LearnToRankClient implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LearnToRankClient.class);

    private final RestClient restClient;

    private static final String LTR_INDEX = "_ltr";

    public LearnToRankClient(RestClientBuilder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public Response get(String apiEndPoint, Map<String, String> params) throws IOException {
        return this.restClient.performRequest(HttpMethod.GET.method(), apiEndPoint, params);
    }

    public Response put(String apiEndPoint, Map<String, String> params, String json) throws IOException {
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        return this.restClient.performRequest(HttpMethod.PUT.method(), apiEndPoint, params, entity);
    }

    public Response post(String apiEndPoint, Map<String, String> params, String json) throws IOException {
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        return this.restClient.performRequest(HttpMethod.POST.method(), apiEndPoint, params, entity);
    }

    public Response delete(String apiEndPoint, Map<String, String> params) throws IOException {
        return this.restClient.performRequest(HttpMethod.DELETE.method(), apiEndPoint, params);
    }

    /**
     *
     * @return Response
     * @throws IOException
     *
     * Initialise the default feature store
     */
    public Response initFeatureStore() throws IOException {
        return this.restClient.performRequest(HttpMethod.PUT.method(), LTR_INDEX, Collections.emptyMap());
    }

    /**
     *
     * @return Response
     * @throws IOException
     *
     * Drops the featurestore
     */
    public Response dropFeatureStore() throws IOException {
        return this.delete(LTR_INDEX, Collections.emptyMap());
    }

    /**
     *
     * @param name
     * @return Response
     * @throws IOException
     *
     * Gets a featureset by its name
     */
    public LearnToRankGetResponse getFeatureSetByName(String name) throws IOException {
        String api = endpoint(LearnToRankEndPoint.FEATURESET.getEndPoint(), name);
        return LearnToRankGetResponse.fromResponse(this.get(api, Collections.emptyMap()));
    }

    public Response deleteFeatureSetByName(String name) throws IOException {
        String api = endpoint(LearnToRankEndPoint.FEATURESET.getEndPoint(), name);
        return this.delete(api, Collections.emptyMap());
    }

    public LearnToRankListResponse listFeatureSets() throws IOException {
        String api = endpoint(LearnToRankEndPoint.FEATURESET);
        Map<String, String> params = Collections.EMPTY_MAP;

        Response response = this.get(api, params);
        return LearnToRankListResponse.fromResponse(response);
    }

    public Response addFeatureSet(FeatureSetRequest request) throws IOException {
        String api = endpoint(LearnToRankEndPoint.FEATURESET.getEndPoint(), request.getName());
        LOGGER.info("Adding featureset with name {} : {}", request.getName(), api);
        return this.put(api, Collections.emptyMap(), request.toJson());
    }

    @Override
    public void close() throws Exception {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Closing RestClient");
        this.restClient.close();
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Closed RestClient");
    }

    // UTIL //

    static String endpoint(String[] indices, String[] types, String endpoint) {
        return endpoint(String.join(",", indices), String.join(",", types), endpoint);
    }

    static String endpoint(LearnToRankEndPoint... learnToRankEndPoints) {
        String[] endPoints = new String[learnToRankEndPoints.length];

        for (int i = 0; i < learnToRankEndPoints.length; i++) {
            endPoints[i] = learnToRankEndPoints[i].getEndPoint();
        }

        return endpoint(endPoints);
    }

    /**
     * Utility method to build request's endpoint.
     */
    static String endpoint(String... parts) {
        StringJoiner joiner = new StringJoiner("/", "/", "");
        // Add the _ltr endpoint
        joiner.add(LTR_INDEX);
        for (String part : parts) {
            if (Strings.hasLength(part)) {
                joiner.add(part);
            }
        }
        return joiner.toString();
    }

    enum LearnToRankEndPoint {
        FEATURESET("_featureset");

        private String endPoint;

        LearnToRankEndPoint(String endPoint) {
            this.endPoint = endPoint;
        }

        public String getEndPoint() {
            return endPoint;
        }
    }

    public static void main(String[] args) {
        try (LearnToRankClient client = LearnToRankHelper.getLTRClient("localhost")) {
//            client.dropFeatureStore();
//            client.initFeatureStore();
            System.out.println(client.getFeatureSetByName("test_more_movie_features").getSource().toJson());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
