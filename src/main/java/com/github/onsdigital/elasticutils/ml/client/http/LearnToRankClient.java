package com.github.onsdigital.elasticutils.ml.client.http;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.apache.http.util.EntityUtils;
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
 *
 * This class implements APIs for working with Elasticsearch Learn to Rank over Http.
 */
public class LearnToRankClient implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LearnToRankClient.class);

    private boolean isRegisteredShutdown = false;
    private final RestClient restClient;

    public LearnToRankClient(RestClientBuilder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public LearnToRankClient(RestClientBuilder restClientBuilder, boolean registerShutdown) {
        this.restClient = restClientBuilder.build();
        if (registerShutdown) {
            this.registerShutdownThread();
        }
    }

    /**
     * Expose GET API from org.elasticsearch.client.RestClient
     * @param apiEndPoint to perform the GET request on
     * @param params additional parameters for the request
     * @return org.elasticsearch.client.Response
     * @throws IOException
     */
    private Response get(String apiEndPoint, Map<String, String> params) throws IOException {
        return this.restClient.performRequest(HttpMethod.GET.method(), apiEndPoint, params);
    }

    /**
     * Expose PUT API without content from org.elasticsearch.client.RestClient
     * @param apiEndPoint to perform the PUT request on
     * @param params additional parameters for the request
     * @return org.elasticsearch.client.Response
     * @throws IOException
     */
    private Response put(String apiEndPoint, Map<String, String> params) throws IOException {
        return this.restClient.performRequest(HttpMethod.PUT.method(), apiEndPoint, params);
    }

    /**
     * Expose PUT API with content from org.elasticsearch.client.RestClient
     * @param apiEndPoint to perform the PUT request on
     * @param params additional parameters for the request
     * @param json json string for the request
     * @return org.elasticsearch.client.Response
     * @throws IOException
     */
    private Response put(String apiEndPoint, Map<String, String> params, String json) throws IOException {
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        return this.restClient.performRequest(HttpMethod.PUT.method(), apiEndPoint, params, entity);
    }

    /**
     * Expose POST API without content from org.elasticsearch.client.RestClient
     * @param apiEndPoint to perform the POST request on
     * @param params additional parameters for the request
     * @return org.elasticsearch.client.Response
     * @throws IOException
     */
    private Response post(String apiEndPoint, Map<String, String> params) throws IOException {
        return this.restClient.performRequest(HttpMethod.POST.method(), apiEndPoint, params);
    }

    /**
     * Expose POST API from org.elasticsearch.client.RestClient
     * @param apiEndPoint to perform the POST request on
     * @param params additional parameters for the request
     * @param json json string for the request
     * @return org.elasticsearch.client.Response
     * @throws IOException
     */
    private Response post(String apiEndPoint, Map<String, String> params, String json) throws IOException {
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        return this.restClient.performRequest(HttpMethod.POST.method(), apiEndPoint, params, entity);
    }

    /**
     * Expose DELETE API from org.elasticsearch.client.RestClient
     * @param apiEndPoint to perform the DELETE request on
     * @param params additional parameters for the request
     * @return org.elasticsearch.client.Response
     * @throws IOException
     */
    private Response delete(String apiEndPoint, Map<String, String> params) throws IOException {
        return this.restClient.performRequest(HttpMethod.DELETE.method(), apiEndPoint, params);
    }

    // FEATURES //

    /**
     * Check whether the feature store has been initialised
     * @param featureStore
     * @return true if the feature store is initialised, false otherwise
     * @throws IOException
     */
    public boolean featureStoreExists(String featureStore) throws IOException {
        String api = endpoint(EndPoint.LTR.getEndPoint(),
                featureStore, EndPoint.FEATURESET.getEndPoint());
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
     * Initialises the feature store
     * @param featureStore
     * @return org.elasticsearch.client.Response
     * @throws IOException
     */
    public Response initFeatureStore(String featureStore) throws IOException {
        if (this.featureStoreExists(featureStore)) {
            LOGGER.debug("Attempt to init FeatureStore which already exists.");
            throw new RuntimeException("FeatureStore already exists");
        }
        String api = endpoint(EndPoint.LTR.getEndPoint(), featureStore);
        return this.put(api, Collections.emptyMap());
    }

    /**
     * Drops the feature store
     * @param featureStore
     * @return org.elasticsearch.client.Response
     * @throws IOException
     */
    public Response dropFeatureStore(String featureStore) throws IOException {
        String api = endpoint(EndPoint.LTR.getEndPoint(), featureStore);
        return this.delete(api, Collections.emptyMap());
    }

    /**
     * Lists all FeatureSets contained in the Elasticsearch feature store
     * @param featureStore
     * @return LearnToRankListResponse which can unmarshall the json response to a FeatureSetRequest object
     * @throws IOException
     */
    public LearnToRankListResponse<FeatureSetRequest> listFeatureSets(String featureStore) throws IOException {
        String api = endpoint(EndPoint.LTR.getEndPoint(),
                featureStore, EndPoint.FEATURESET.getEndPoint());
        Map<String, String> params = Collections.EMPTY_MAP;

        Response response = this.get(api, params);
        return LearnToRankListResponse.fromResponse(response, new TypeReference<LearnToRankListResponse<FeatureSetRequest>>(){});
    }

    /**
     * Checks if a feature set with the given name exists
     * @param featureStore
     * @param name name of the feature set
     * @return true if exists, else false
     * @throws IOException
     */
    public boolean featureSetExists(String featureStore, String name) throws IOException {
        try {
            LearnToRankGetResponse response = this.getFeatureSet(featureStore, name);
            return response.isFound();
        } catch (ResponseException e) {
            return false;
        }
    }

    /**
     * Retrieves a FeatureSet from Elasticsearch by its name
     * @param featureStore
     * @param name
     * @return LearnToRankGetResponse to unmarshall the json response
     * @throws IOException
     */
    public LearnToRankGetResponse<FeatureSetRequest> getFeatureSet(String featureStore, String name) throws IOException {
        String api = endpoint(EndPoint.LTR.getEndPoint(),
                featureStore, EndPoint.FEATURESET.getEndPoint(), name);
        Response response = this.get(api, Collections.emptyMap());

        String entity = EntityUtils.toString(response.getEntity());
        return JsonUtils.MAPPER.readValue(entity, new TypeReference<LearnToRankGetResponse<FeatureSetRequest>>(){});
    }

    /**
     * API to create a feature set
     * @param request
     * @return org.elasticsearch.client.Response
     * @throws IOException
     */
    public Response createFeatureSet(String featureStore, FeatureSetRequest request) throws IOException {
        if (!this.featureStoreExists(featureStore)) {
            throw new IOException("Unknown featureStore: " + featureStore);
        }
        String api = endpoint(EndPoint.LTR.getEndPoint(),
                featureStore, EndPoint.FEATURESET.getEndPoint(), request.getName());
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Adding featureset with name {} : {}", request.getName(), api);
        return this.put(api, Collections.emptyMap(), request.toJson());
    }

    /**
     * API to delete a feature set
     * @param name
     * @return org.elasticsearch.client.Response
     * @throws IOException
     */
    public Response deleteFeatureSet(String featureStore, String name) throws IOException {
        String api = endpoint(EndPoint.LTR.getEndPoint(),
                featureStore, EndPoint.FEATURESET.getEndPoint(), name);
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Deleting featureset with name {} : {}", name, api);
        return this.delete(api, Collections.emptyMap());
    }

    /**
     * API to append feature(s) to an existing feature set
     * @param name
     * @param featureList
     * @return
     * @throws IOException
     */
    public Response appendToFeatureSet(String featureStore, String name, LinkedList<Feature> featureList) throws IOException {
        String api = endpoint(EndPoint.LTR.getEndPoint(),
                featureStore, EndPoint.FEATURESET.getEndPoint(), name, EndPoint.ADD_FEATURES.getEndPoint());
        FeatureSet featureSet = new FeatureSet(null, featureList);

        if (LOGGER.isDebugEnabled()) LOGGER.debug("Appending to featureset with name {} : {}", name, api);
        String json = featureSet.toJson();
        return this.post(api, Collections.emptyMap(), json);
    }

    // MODEL CRUD //

    /**
     * Returns the cache stats for the LTR index
     * @return
     */
    public Response getCacheStats() throws IOException {
        String api = endpoint(EndPoint.LTR, EndPoint.CACHE_STATS);
        return this.get(api, Collections.emptyMap());
    }

    /**
     * Clears the LTR model cache
     * @return org.elasticsearch.client.Response
     * @throws IOException
     */
    public Response clearCache(String featureStore) throws IOException {
        String api = endpoint(EndPoint.LTR.getEndPoint(), featureStore, EndPoint.CLEAR_CACHE.getEndPoint());
        return this.post(api, Collections.emptyMap());
    }

    public LearnToRankListResponse<RankLibModel> listModels(String featureStore) throws IOException {
        String api = endpoint(EndPoint.LTR.getEndPoint(), featureStore, EndPoint.MODEL.getEndPoint());

        Response response = this.get(api, Collections.emptyMap());
        return LearnToRankListResponse.fromResponse(response, new TypeReference<LearnToRankListResponse<RankLibModel>>(){});
    }

    public Response getModel(String featureStore, String name) throws IOException {
        String api = endpoint(EndPoint.LTR.getEndPoint(), featureStore, EndPoint.MODEL.getEndPoint(), name);

        return this.get(api, Collections.emptyMap());
    }

    public Response createModel(String featureStore, String featureSet, RankLibModel model) throws IOException {
        final Map<String, RankLibModel> request = new HashMap<String, RankLibModel>() {{
            put("model", model);
        }};

        String api = endpoint(EndPoint.LTR.getEndPoint(), featureStore, EndPoint.FEATURESET.getEndPoint(),
                featureSet, EndPoint.CREATE_MODEL.getEndPoint());

        return this.post(api, Collections.emptyMap(), JsonUtils.toJson(request));
    }

    public Response deleteModel(String featureStore, String name) throws IOException {
        String api = endpoint(EndPoint.LTR.getEndPoint(), featureStore, EndPoint.MODEL.getEndPoint(), name);

        return this.delete(api, Collections.emptyMap());
    }

    // SLTR SEARCH //

    /**
     * API to perform a search request and log features against a given feature set using a LogQuerySearchRequest
     * @param index
     * @param logQuery LogQuerySearchRequest object to provide the json request
     * @return SltrResponse to unmarshall the json response
     * @throws IOException
     */
    public SltrResponse search(String index, LogQuerySearchRequest logQuery) throws IOException {
        String api = endpoint(index, Operation.SEARCH.getOperation());
        String jsonRequest = logQuery.toJson();
        Response response = this.post(api, Collections.emptyMap(), jsonRequest);
        return SltrResponse.fromResponse(response);
    }

    /**
     * Method to register a shutdown hook for this client
     */
    public void registerShutdownThread() {
        if (!this.isRegisteredShutdown) {
            Runtime.getRuntime().addShutdownHook(new ShutDownClientThread(this));
            this.isRegisteredShutdown = true;
        }
    }

    /**
     * Implements auto-closable on the RestClient
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Closing RestClient");
        this.restClient.close();
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Closed RestClient");
    }

    // UTIL //

    /**
     * Utility function to construct API endpoints as Strings
     * @param indices
     * @param types
     * @param endpoint
     * @return
     */
    static String endpoint(String[] indices, String[] types, String endpoint) {
        return endpoint(String.join(",", indices), String.join(",", types), endpoint);
    }

    /**
     * Utility function to construct API endpoints as Strings
     * @param learnToRankEndPoints
     * @return
     */
    static String endpoint(EndPoint... learnToRankEndPoints) {
        String[] endPoints = new String[learnToRankEndPoints.length];

        for (int i = 0; i < learnToRankEndPoints.length; i++) {
            endPoints[i] = learnToRankEndPoints[i].getEndPoint();
        }

        return endpoint(endPoints);
    }

    /**
     * Utility function to construct API endpoints as Strings
     * @param parts
     * @return
     */
    static String endpoint(String... parts) {
        StringJoiner joiner = new StringJoiner("/", "/", "");
        for (String part : parts) {
            if (Strings.hasLength(part)) {
                joiner.add(part);
            }
        }
        return joiner.toString();
    }

    /**
     * Enum containing frequently used LTR endpoints
     */
    enum EndPoint {
        LTR("_ltr"),
        FEATURESET("_featureset"),
        ADD_FEATURES("_addfeatures"),
        CREATE_MODEL("_createmodel"),
        MODEL("_model"),
        CACHE_STATS("_cachestats"),
        CLEAR_CACHE("_clearcache");

        String endPoint;

        EndPoint(String endPoint) {
            this.endPoint = endPoint;
        }

        public String getEndPoint() {
            return endPoint;
        }
    }

    /**
     * Enum to provide a clean way to access various operations
     */
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

    /**
     * Thread which allows the user to register a client shutdown
     */
    public static class ShutDownClientThread extends Thread {

        private static final Logger LOGGER = LoggerFactory.getLogger(ShutDownClientThread.class);

        private LearnToRankClient client;

        public ShutDownClientThread(LearnToRankClient client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                client.close();
            } catch (Exception e) {
                LOGGER.error("Failed to shut down LearnToRankClient.", e);
            }
        }
    }

}
