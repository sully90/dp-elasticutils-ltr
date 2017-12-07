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

    public LearnToRankClient getClient() {
        return LearnToRankHelper.getLTRClient(HOSTNAME);
    }

    @Test
    public void testFeatureSetCreation() {
        String name = "match_query";
        String field = "title";

        Feature feature = null;
        Feature newFeature = null;
        try {
            feature = Feature.matchFeature(name, field);
            newFeature = Feature.matchFeature(name + "_new", field);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        LinkedList<Feature> features = new LinkedList<Feature>();
        LinkedList<Feature> newFeatures = new LinkedList<Feature>();
        features.add(feature);
        newFeatures.add(newFeature);

        FeatureSet featureSet = new FeatureSet("java-test-feature-set", features);
        FeatureSetRequest request = new FeatureSetRequest(featureSet);

        try {
            System.out.println(request.toJson());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

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
