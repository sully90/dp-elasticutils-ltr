//package com.github.onsdigital.elasticutils.ml.client;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.github.onsdigital.elasticutils.client.ElasticSearchRESTClient;
//import com.github.onsdigital.elasticutils.client.bulk.configuration.BulkProcessorConfiguration;
//import com.github.onsdigital.elasticutils.ml.features.Feature;
//import com.github.onsdigital.elasticutils.ml.features.FeatureSet;
//import com.github.onsdigital.elasticutils.ml.features.Template;
//import com.github.onsdigital.elasticutils.ml.requests.FeatureSetRequest;
//import com.github.onsdigital.elasticutils.ml.util.ResponseParser;
//import org.apache.http.HttpEntity;
//import org.apache.http.entity.ContentType;
//import org.apache.http.nio.entity.NStringEntity;
//import org.apache.http.util.EntityUtils;
//import org.elasticsearch.action.get.GetRequest;
//import org.elasticsearch.action.get.GetResponse;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.Request;
//import org.elasticsearch.client.Response;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//
//import java.io.IOException;
//import java.util.*;
//
///**
// * @author sullid (David Sullivan) on 23/11/2017
// * @project dp-elasticutils-ltr
// *
// * A RESTful client designed to work with Learn to Rank
// */
//public class ElasticSearchLtrRESTClient<T> extends ElasticSearchRESTClient {
//
//    private final ResponseParser responseParser;
//
//    public ElasticSearchLtrRESTClient(String hostName, Class returnClass) {
//        super(hostName, LearnToRankAPI.LTR_API, returnClass);
//        this.responseParser = new ResponseParser(this.getLowLevelClient());
//    }
//
//    public ElasticSearchLtrRESTClient(String hostName, int http_port, Class returnClass) {
//        super(hostName, http_port, LearnToRankAPI.LTR_API, returnClass);
//        this.responseParser = new ResponseParser(this.getLowLevelClient());
//
//    }
//
//    public ElasticSearchLtrRESTClient(String hostName, int http_port, BulkProcessorConfiguration bulkProcessorConfiguration, Class returnClass) {
//        super(hostName, http_port, LearnToRankAPI.LTR_API, bulkProcessorConfiguration, returnClass);
//        this.responseParser = new ResponseParser(this.getLowLevelClient());
//    }
//
//    public ElasticSearchLtrRESTClient(RestHighLevelClient client, BulkProcessorConfiguration bulkProcessorConfiguration, Class returnClass) {
//        super(LearnToRankAPI.LTR_API, client, bulkProcessorConfiguration, returnClass);
//        this.responseParser = new ResponseParser(this.getLowLevelClient());
//    }
//
//    // FEATURE SET CRUD //
//
//    public Response initFeatureStore() throws IOException {
//        Response response = super.getLowLevelClient().performRequest(HttpRequestType.PUT.getRequestType(), indexName);
//        return response;
//    }
//
//    public SearchResponse listFeatureSets() throws IOException {
////        RestClient client = super.getLowLevelClient();
////        Map<String, String> params = Collections.emptyMap();
////
////        String api = LearnToRankAPI.LTR_API + "/" + LeanToRankAPIEndpoint.FEATURESET.getEndPoint();
////
////        Response response = client.performRequest(HttpRequestType.GET.getRequestType(), api, params);
////        HttpEntity entity = response.getEntity();
////        System.out.println(EntityUtils.toString(entity));
////        return RESPONSE_PARSER.parseEntity(entity, SearchResponse::fromXContent);
//
//        String api = LearnToRankAPI.LTR_API + "/" + LeanToRankAPIEndpoint.FEATURESET.getEndPoint();
//        GetRequest getRequest = new GetRequest()
//                .index(api)
//                .type("store");
//        GetResponse response = this.responseParser.performRequestAndParseEntity(getRequest, Request::get, )
//    }
//
//    public Response addFeatureSet(FeatureSetRequest request) throws IOException {
//        RestClient client = super.getLowLevelClient();
//        Map<String, String> params = Collections.emptyMap();
//        String requestJson = request.toJson();
//
//        String api = LearnToRankAPI.getFeatureApi(request.getFeatureSet().getName());
//
//        HttpEntity entity = new NStringEntity(requestJson, ContentType.APPLICATION_JSON);
//        Response response = client.performRequest(HttpRequestType.POST.getRequestType(), api, params, entity);
//        return response;
//    }
//
//    public Response addToExistingFeatureSet(String name, FeatureSet featureSet) throws IOException {
//        RestClient client = super.getLowLevelClient();
//        Map<String, String> params = Collections.emptyMap();
//
//        FeatureSetRequest request = new FeatureSetRequest(featureSet);
//        String requestJson = request.toJson();
//
//        String api = LearnToRankAPI.getFeatureApi(name) + "/" + LeanToRankAPIEndpoint.ADD_FEATURES.getEndPoint();
//
//        HttpEntity entity = new NStringEntity(requestJson, ContentType.APPLICATION_JSON);
//        Response response = client.performRequest(HttpRequestType.POST.getRequestType(), api, params, entity);
//        return response;
//    }
//
//    public Response deleteFeatureSet(FeatureSetRequest request) throws IOException {
//        RestClient client = super.getLowLevelClient();
//
//        String api = LearnToRankAPI.getFeatureApi(request.getFeatureSet().getName());
//        Response response = client.performRequest(HttpRequestType.DELETE.getRequestType(), api);
//        return response;
//    }
//
//    public Response deleteFeatureStore() throws IOException {
//        Response response = super.getLowLevelClient().performRequest(HttpRequestType.DELETE.getRequestType(), LearnToRankAPI.LTR_API);
//        return response;
//    }
//
//    public static String getResponseBody(Response response) throws IOException {
//        return EntityUtils.toString(response.getEntity());
//    }
//
//    private static Map<String, Object> fromJsonString(String json) throws IOException {
//        Map<String, Object> jsonMap = new HashMap<>();
//        jsonMap = MAPPER.readValue(json, new TypeReference<Map<String, Object>>(){});
//        return jsonMap;
//    }
//
//    public static class LearnToRankAPI {
//        private static final String LTR_API = "_ltr";
//
//        public static String getFeatureApi(String featureName) {
//            StringBuilder sb = new StringBuilder(LTR_API)
//                    .append("/")
//                    .append(LeanToRankAPIEndpoint.FEATURESET.getEndPoint())
//                    .append("/")
//                    .append(featureName);
//            return sb.toString();
//        }
//
//    }
//
//    public enum LeanToRankAPIEndpoint {
//        FEATURESET("_featureset"),
//        ADD_FEATURES("_addFeatures");
//
//        private String endPoint;
//
//        LeanToRankAPIEndpoint(String endPoint) {
//            this.endPoint = endPoint;
//        }
//
//        public String getEndPoint() {
//            return endPoint;
//        }
//    }
//
//    private static FeatureSetRequest generateDefaultRequest() {
//        Map<String, String> templateMatch = new HashMap<String, String>() {{
//            put("title", "{{keywords}}");
//            put("overview", "{{keywords}}");
//        }};
//        Template template = new Template(templateMatch);
//
//        List<String> params = new ArrayList<String>() {{
//            add("keywords");
//        }};
//        final Feature feature = new Feature("title_query", params, template);
//
//        List<Feature> featureList = new ArrayList<Feature>() {{
//            add(feature);
//        }};
//
//        FeatureSet featureSet = new FeatureSet("java_test_feature_set", featureList);
//
//        FeatureSetRequest request = FeatureSetRequest.builder().featureSet(featureSet).build();
//        return request;
//    }
//
//    public static void main(String[] args) {
//        ObjectMapper mapper = new ObjectMapper();
//        try (ElasticSearchLtrRESTClient client = new ElasticSearchLtrRESTClient("localhost", Object.class)) {
////            FeatureSetRequest featureSetRequest = generateDefaultRequest();
//            SearchResponse response = client.listFeatureSets();
//            System.out.println(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
