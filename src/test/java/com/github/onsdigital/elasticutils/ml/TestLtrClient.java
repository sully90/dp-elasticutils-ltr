package com.github.onsdigital.elasticutils.ml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.onsdigital.elasticutils.ml.client.http.LearnToRankClient;
import com.github.onsdigital.elasticutils.ml.features.Feature;
import com.github.onsdigital.elasticutils.ml.features.FeatureSet;
import com.github.onsdigital.elasticutils.ml.requests.FeatureSetRequest;
import com.github.onsdigital.elasticutils.ml.util.LearnToRankHelper;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public void testFeaturesetCreation() {
        QueryBuilder qb = QueryBuilders.matchQuery("title", "{{keywords}}");
        Map<String, Object> template = null;
        try {
            template = LearnToRankHelper.templateMapFromQueryBuilder(qb);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

        String name = "match_query";
        String[] params = new String[] {
          "keywords"
        };
        Feature feature = new Feature(name, Arrays.asList(params), template);
        List<Feature> features = new ArrayList<Feature>() {{
           add(feature);
        }};

        FeatureSet featureSet = new FeatureSet("java-test-feature-set", features);
        FeatureSetRequest request = new FeatureSetRequest(featureSet);

        try {
            System.out.println(request.toJson());
        } catch (JsonProcessingException e) {
            Assert.fail(e.getMessage());
        }

        try (LearnToRankClient client = getClient()) {
            client.addFeatureSet(request);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

}
