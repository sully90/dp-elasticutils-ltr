package com.github.onsdigital.elasticutils.ml.client.http;

import com.github.onsdigital.elasticutils.ml.client.response.features.LearnToRankGetResponse;
import com.github.onsdigital.elasticutils.ml.client.response.features.LearnToRankListResponse;
import com.github.onsdigital.elasticutils.ml.client.response.features.models.Feature;
import com.github.onsdigital.elasticutils.ml.client.response.features.models.FeatureSet;
import com.github.onsdigital.elasticutils.ml.client.response.sltr.SltrResponse;
import com.github.onsdigital.elasticutils.ml.ranklib.models.RankLibModel;
import com.github.onsdigital.elasticutils.ml.requests.FeatureSetRequest;
import com.github.onsdigital.elasticutils.ml.requests.LogQuerySearchRequest;
import com.github.onsdigital.elasticutils.ml.util.JsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.common.Strings;
import org.elasticsearch.xpack.common.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

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

    // FEATURES //

    /**
     *
     * @return true if featureStore exists, false otherwise
     * @throws IOException
     */
    public boolean featureStoreExists() throws IOException {
        String api = endpoint(true, LearnToRankEndPoint.FEATURESET);
        try {
            Response response = this.get(api, Collections.emptyMap());
            return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
        } catch (ResponseException e) {
            if (e.getResponse().getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                return false;
            } else {
                LOGGER.error("Error checking if featureStore exists", e);
                throw new IOException(e);
            }
        }
    }

    /**
     *
     * @return Response
     * @throws IOException
     *
     * Initialise the default feature store
     */
    public Response initFeatureStore() throws IOException {
        if (this.featureStoreExists()) {
            LOGGER.debug("Attempt to init FeatureStore which already exists.");
            throw new RuntimeException("FeatureStore already exists");
        }
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
        String api = endpoint(true, LearnToRankEndPoint.FEATURESET.getEndPoint(), name);
        return LearnToRankGetResponse.fromResponse(this.get(api, Collections.emptyMap()));
    }

    public Response deleteFeatureSetByName(String name) throws IOException {
        String api = endpoint(true, LearnToRankEndPoint.FEATURESET.getEndPoint(), name);
        return this.delete(api, Collections.emptyMap());
    }

    public LearnToRankListResponse listFeatureSets() throws IOException {
        String api = endpoint(true, LearnToRankEndPoint.FEATURESET);
        Map<String, String> params = Collections.EMPTY_MAP;

        Response response = this.get(api, params);
        return LearnToRankListResponse.fromResponse(response);
    }

    public Response addFeatureSet(FeatureSetRequest request) throws IOException {
        String api = endpoint(true, LearnToRankEndPoint.FEATURESET.getEndPoint(), request.getName());
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Adding featureset with name {} : {}", request.getName(), api);
        return this.put(api, Collections.emptyMap(), request.toJson());
    }

    public Response addToFeatureSet(String name, List<Feature> featureList) throws IOException {
        String api = endpoint(true, LearnToRankEndPoint.FEATURESET.getEndPoint(), name, LearnToRankEndPoint.ADD_FEATURES.getEndPoint());
        FeatureSet featureSet = new FeatureSet(null, featureList);

        String json = featureSet.toJson();
        return this.post(api, Collections.emptyMap(), json);
    }

    // MODEL CRUD //

    public Response uploadModel(String featureSet, RankLibModel model) throws IOException {
        final Map<String, RankLibModel> request = new HashMap<String, RankLibModel>() {{
            put("model", model);
        }};

        String api = endpoint(true, LearnToRankEndPoint.FEATURESET.getEndPoint(),
                featureSet, LearnToRankEndPoint.CREATE_MODEL.getEndPoint());

        return this.post(api, Collections.emptyMap(), JsonUtils.toJson(request));
    }

    // SLTR SEARCH //

    public SltrResponse sltr(String index, LogQuerySearchRequest logQuery, int size) throws IOException {
        String api = endpoint(false, Operation.SEARCH.getOperation());
        String jsonRequest = logQuery.toJsonWithSize(size);
        Response response = this.post(api, Collections.emptyMap(), jsonRequest);
        return SltrResponse.fromResponse(response);
    }

    @Override
    public void close() throws Exception {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Closing RestClient");
        this.restClient.close();
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Closed RestClient");
    }

    // UTIL //

    static String endpoint(boolean featureCrud, String[] indices, String[] types, String endpoint) {
        return endpoint(featureCrud, String.join(",", indices), String.join(",", types), endpoint);
    }

    static String endpoint(boolean featureCrud, LearnToRankEndPoint... learnToRankEndPoints) {
        String[] endPoints = new String[learnToRankEndPoints.length];

        for (int i = 0; i < learnToRankEndPoints.length; i++) {
            endPoints[i] = learnToRankEndPoints[i].getEndPoint();
        }

        return endpoint(featureCrud, endPoints);
    }

    /**
     * Utility method to build request's endpoint.
     */
    static String endpoint(boolean featureCrud, String... parts) {
        StringJoiner joiner = new StringJoiner("/", "/", "");
        if (featureCrud) {
            // Add the base LTR index
            joiner.add(LTR_INDEX);
        }
        for (String part : parts) {
            if (Strings.hasLength(part)) {
                joiner.add(part);
            }
        }
        return joiner.toString();
    }

    enum LearnToRankEndPoint {
        FEATURESET("_featureset"),
        ADD_FEATURES("_addfeatures"),
        CREATE_MODEL("_createmodel");

        private String endPoint;

        LearnToRankEndPoint(String endPoint) {
            this.endPoint = endPoint;
        }

        public String getEndPoint() {
            return endPoint;
        }
    }

    enum Operation {
        SEARCH("_search");

        private String operation;

        Operation(String operation) {
            this.operation = operation;
        }

        public String getOperation() {
            return operation;
        }
    }

}
