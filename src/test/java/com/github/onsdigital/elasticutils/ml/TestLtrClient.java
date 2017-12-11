package com.github.onsdigital.elasticutils.ml;

import com.github.onsdigital.elasticutils.ml.client.http.LearnToRankClient;
import com.github.onsdigital.elasticutils.ml.client.response.features.LearnToRankGetResponse;
import com.github.onsdigital.elasticutils.ml.client.response.features.models.Feature;
import com.github.onsdigital.elasticutils.ml.client.response.features.models.FeatureSet;
import com.github.onsdigital.elasticutils.ml.requests.FeatureSetRequest;
import com.github.onsdigital.elasticutils.ml.util.LearnToRankHelper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 * @author sullid (David Sullivan) on 04/12/2017
 * @project dp-elasticutils-ltr
 */
public class TestLtrClient {

    private static final String HOSTNAME = "localhost";

    private static String FEATURESET_NAME = "java-test-feature-set";
    private static final String NAME = "match_query";
    private static final String FIELD = "title";

    public LearnToRankClient getClient() {
        return LearnToRankHelper.getLTRClient(HOSTNAME);
    }

    private static FeatureSetRequest getFeatureSetRequest() {
        Feature feature = null;
        try {
            feature = Feature.matchFeature(NAME, FIELD);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        LinkedList<Feature> features = new LinkedList<Feature>();

        features.add(feature);

        FeatureSet featureSet = new FeatureSet(FEATURESET_NAME, features);
        FeatureSetRequest request = new FeatureSetRequest(featureSet);

        return request;
    }

    @Test
    public void testFeatureSetCreation() {

        FeatureSetRequest request = getFeatureSetRequest();
        try {
            System.out.println(request.toJson());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

        Feature newFeature = null;
        try {
            newFeature = Feature.matchFeature(NAME + "_new", FIELD);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        LinkedList<Feature> newFeatures = new LinkedList<Feature>();
        newFeatures.add(newFeature);

        try (LearnToRankClient client = getClient()) {
            boolean featureStoreExists = client.featureStoreExists();

            if (!featureStoreExists) {
                client.initFeatureStore();
            }
            client.createFeatureSet(request);

            LearnToRankGetResponse response = client.getFeatureSetByName(request.getName());
            assertEquals(response.getSource().getFeatureSet(), request.getFeatureSet());

            // Assert we only have one feature
            assertEquals(response.getSource().getFeatureSet().getFeatureList().size(), 1);

            // Add a new feature
            client.appendToFeatureSet(request.getName(), newFeatures);
            // Perform the GET again
            response = client.getFeatureSetByName(request.getName());
            // Assert the new feature was added
            assertEquals(response.getSource().getFeatureSet().getFeatureList().size(), 2);

            // Delete
            client.deleteFeatureSetByName(request.getName());

            if (!featureStoreExists) {
                // Cleanup
                client.dropFeatureStore();
            }
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

}
