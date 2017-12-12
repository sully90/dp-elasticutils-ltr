//package com.github.onsdigital.elasticutils.ml.old;
//
//import com.github.onsdigital.elasticutils.ml.client.http.LearnToRankClient;
//import com.github.onsdigital.elasticutils.ml.client.response.features.LearnToRankGetResponse;
//import com.github.onsdigital.elasticutils.ml.client.response.features.models.Feature;
//import com.github.onsdigital.elasticutils.ml.client.response.features.models.FeatureSet;
//import com.github.onsdigital.elasticutils.ml.ranklib.models.ModelType;
//import com.github.onsdigital.elasticutils.ml.ranklib.models.RankLibModel;
//import com.github.onsdigital.elasticutils.ml.requests.FeatureSetRequest;
//import com.github.onsdigital.elasticutils.ml.util.JsonUtils;
//import com.github.onsdigital.elasticutils.ml.util.LearnToRankHelper;
//import org.apache.http.HttpStatus;
//import org.elasticsearch.client.Response;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.util.LinkedList;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * @author sullid (David Sullivan) on 11/12/2017
// * @project dp-elasticutils-ltr
// */
//public class TestModels {
//
//    private static final String HOSTNAME = "localhost";
//    private static final String MODEL_NAME = "test_6";
//
//    private static String FEATURESET_NAME = "java-test-feature-set-model";
//    private static final String NAME = "match_query";
//    private static final String FIELD = "title";
//
//    public static String getModelFilename() throws IOException {
//        String filename = "/Users/sullid/ONS/dp-elasticutils-ltr/src/main/resources/models/test_6_model.txt";
//        return filename;
//    }
//
//    public static LearnToRankClient getClient() {
//        return LearnToRankHelper.getLTRClient(HOSTNAME);
//    }
//
//    public static FeatureSetRequest getFeatureSetRequest() {
//        Feature feature = null;
//        try {
//            feature = Feature.matchFeature(NAME, FIELD);
//        } catch (IOException e) {
//            Assert.fail(e.getMessage());
//        }
//        LinkedList<Feature> features = new LinkedList<Feature>();
//
//        features.add(feature);
//
//        FeatureSet featureSet = new FeatureSet(FEATURESET_NAME, features);
//        FeatureSetRequest request = new FeatureSetRequest(featureSet);
//
//        return request;
//    }
//
//    @Before
//    public void uploadModel() {
//        // Create test featureSet and model
//        try (LearnToRankClient client = getClient()) {
//            String filename = getModelFilename();
//            String content = RankLibModel.getDefinition(filename);
//            RankLibModel model = new RankLibModel(MODEL_NAME, ModelType.RANKLIB, content);
//
//            // Test featureset
//            FeatureSetRequest featureSetRequest = getFeatureSetRequest();
//
//            boolean featureStoreExists = client.featureStoreExists();
//            System.out.println("Feature store exists: " + featureStoreExists);
//
//            if (!featureStoreExists) {
//                client.initFeatureStore();
//            }
//            client.createFeatureSet(featureSetRequest);
//
//            LearnToRankGetResponse<FeatureSetRequest> response = client.getFeatureSet(featureSetRequest.getName());
//            assertEquals(response.getSource().getFeatureSet(), featureSetRequest.getFeatureSet());
//
//            // Upload the model against this featureset
//            Response uploadResponse = client.createModel(featureSetRequest.getName(), model);
//            assertEquals(uploadResponse.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
//        } catch (IOException e) {
//            Assert.fail(e.getMessage());
//        } catch (Exception e) {
//            Assert.fail(e.getMessage());
//        }
//    }
//
//    @Test
//    public void testModelList() {
//        try (LearnToRankClient client = getClient()) {
//            Response response = client.listModels();
//            assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
//        } catch (Exception e) {
//            Assert.fail(e.getMessage());
//        }
//    }
//
//    @After
//    public void deleteModelAndFeatureset() {
//        // Delete test featureSet and model
//        String featureSetName = FEATURESET_NAME;
//
//        try (LearnToRankClient client = getClient()) {
//            Response response = client.deleteModel(MODEL_NAME);
//            assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
//
//            Response deleteFeaturesetResponse = client.deleteFeatureSet(featureSetName);
//            assertEquals(deleteFeaturesetResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
//        } catch (Exception e) {
//            Assert.fail(e.getMessage());
//        }
//    }
//
//}
