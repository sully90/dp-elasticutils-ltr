package com.github.onsdigital.elasticutils.ml;

import com.github.onsdigital.elasticutils.ml.client.http.LearnToRankClient;
import com.github.onsdigital.elasticutils.ml.client.response.features.LearnToRankListResponse;
import com.github.onsdigital.elasticutils.ml.client.response.features.models.Feature;
import com.github.onsdigital.elasticutils.ml.client.response.features.models.FeatureSet;
import com.github.onsdigital.elasticutils.ml.requests.FeatureSetRequest;
import com.github.onsdigital.elasticutils.ml.utils.ClientUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * @author sullid (David Sullivan) on 12/12/2017
 * @project dp-elasticutils-ltr
 */
public class TestFeatureStore {

    private static String FEATURESET_NAME = "java-test-feature-set";
    private static final String NAME = "match_query";
    private static final String FIELD = "title";

    /**
     * Creates a test FeatureSet object and corresponding request
     * @return
     */
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

    @Before
    public void resetFeatureStore() {
        try (LearnToRankClient client = ClientUtils.getClient()) {
            if (client.featureStoreExists()) {
                client.dropFeatureStore();
            }
            assertFalse(client.featureStoreExists());

            client.initFeatureStore();
            assertTrue(client.featureStoreExists());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testLtrClient() {
        try (LearnToRankClient client = ClientUtils.getClient()) {
            // Create and store a test featureset
            FeatureSetRequest request = getFeatureSetRequest();

            client.createFeatureSet(request);

            LearnToRankListResponse<FeatureSetRequest> listResponse = client.listFeatureSets();
            assertEquals(listResponse.getHits().getTotal(), 1);
            assertEquals(listResponse.getHits().getHits().size(), 1);

            FeatureSetRequest originalRequest = listResponse.getHits().getHits().get(0).getSource();
            assertEquals(originalRequest.getName(), request.getName());
            assertEquals(originalRequest.getFeatureSet(), request.getFeatureSet());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @After
    public void tearDown() {
        try (LearnToRankClient client = ClientUtils.getClient()) {
            client.dropFeatureStore();
            assertFalse(client.featureStoreExists());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

}
