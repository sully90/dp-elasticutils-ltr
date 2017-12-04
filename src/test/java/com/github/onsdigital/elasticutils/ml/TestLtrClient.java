package com.github.onsdigital.elasticutils.ml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.onsdigital.elasticutils.ml.client.http.LearnToRankClient;
import com.github.onsdigital.elasticutils.ml.client.http.response.LearnToRankGetResponse;
import com.github.onsdigital.elasticutils.ml.features.Feature;
import com.github.onsdigital.elasticutils.ml.features.FeatureSet;
import com.github.onsdigital.elasticutils.ml.requests.FeatureSetRequest;
import com.github.onsdigital.elasticutils.ml.util.LearnToRankHelper;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author sullid (David Sullivan) on 04/12/2017
 * @project dp-elasticutils-ltr
 */
public class TestLtrClient {

    private static final String HOSTNAME = "localhost";

    public LearnToRankClient getClient() {
        return LearnToRankHelper.getLTRClient(HOSTNAME);
    }

    @Before
    public void initFeatureStore() {
        try (LearnToRankClient client = getClient()) {
            if (client.featureStoreExists()) {
                client.dropFeatureStore();
            }
            client.initFeatureStore();
            assertTrue(client.featureStoreExists());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @After
    public void dropFeatureStore() {
        try (LearnToRankClient client = getClient()) {
            client.dropFeatureStore();
            assertFalse(client.featureStoreExists());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
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
        List<Feature> features = new ArrayList<Feature>();
        List<Feature> newFeatures = new ArrayList<Feature>();
        features.add(feature);
        newFeatures.add(newFeature);

        FeatureSet featureSet = new FeatureSet("java-test-feature-set", features);
        FeatureSetRequest request = new FeatureSetRequest(featureSet);

        try {
            System.out.println(request.toJson());
        } catch (JsonProcessingException e) {
            Assert.fail(e.getMessage());
        }

        try (LearnToRankClient client = getClient()) {
            client.addFeatureSet(request);

            LearnToRankGetResponse response = client.getFeatureSetByName(request.getName());
            assertEquals(response.getSource().getFeatureSet(), request.getFeatureSet());

            // Assert we only have one feature
            assertEquals(response.getSource().getFeatureSet().getFeatureList().size(), 1);

            // Add a new feature
            client.addToFeatureSet(request.getName(), newFeatures);
            // Perform the GET again
            response = client.getFeatureSetByName(request.getName());
            // Assert the new feature was added
            assertEquals(response.getSource().getFeatureSet().getFeatureList().size(), 2);

            // Delete
            client.deleteFeatureSetByName(request.getName());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

}
