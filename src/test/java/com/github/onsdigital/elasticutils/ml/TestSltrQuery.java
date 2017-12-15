package com.github.onsdigital.elasticutils.ml;

import com.github.onsdigital.elasticutils.ml.client.http.LearnToRankClient;
import com.github.onsdigital.elasticutils.ml.client.response.sltr.SltrResponse;
import com.github.onsdigital.elasticutils.ml.client.response.sltr.models.Fields;
import com.github.onsdigital.elasticutils.ml.models.TmdbMovie;
import com.github.onsdigital.elasticutils.ml.query.SltrQueryBuilder;
import com.github.onsdigital.elasticutils.ml.requests.LogQuerySearchRequest;
import com.github.onsdigital.elasticutils.ml.utils.ClientUtils;
import com.github.onsdigital.elasticutils.ml.utils.IOUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author sullid (David Sullivan) on 12/12/2017
 * @project dp-elasticutils-ltr
 */
public class TestSltrQuery {

    // Index to test against
    private static final String INDEX = "tmdb";
    private static String FEATURESTORE_NAME = "test_featurestore";

    // ids to use for training
    private static final List<Integer> ids = new ArrayList<Integer>() {{
        add(7555);
        add(1370);
        add(1369);
    }};

    // expected scores
    private static final List<List<Float>> expectedValues;

    static  {
        final List<Float> values1 = new LinkedList<Float>() {{
            add(Float.valueOf(12.318446f));
            add(Float.valueOf(9.8376875f));
        }};
        final List<Float> values2 = new LinkedList<Float>() {{
            add(Float.valueOf(6.8449354f));
            add(Float.valueOf(10.7808075f));
        }};
        final List<Float> values3 = new LinkedList<Float>() {{
            add(Float.valueOf(9.510193f));
            add(Float.valueOf(10.7808075f));
        }};

        expectedValues = new LinkedList<List<Float>>() {{
            add(values1);
            add(values2);
            add(values3);
        }};
    }

    @Before
    public void resetFeatureStoreAndTrainModels() {
        try (LearnToRankClient client = ClientUtils.getClient()) {
            if (client.featureStoreExists(FEATURESTORE_NAME)) {
                client.dropFeatureStore(FEATURESTORE_NAME);
            }
            assertFalse(client.featureStoreExists(FEATURESTORE_NAME));

            // Init new featurestore
            client.initFeatureStore(FEATURESTORE_NAME);
            assertTrue(client.featureStoreExists(FEATURESTORE_NAME));

            // Train our models
            int exitCode = IOUtils.run();
            assertEquals(exitCode, 0);

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testSltr() {
        try (LearnToRankClient client = ClientUtils.getClient()) {
            QueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("_id", "7555", "1370", "1369");

            SltrQueryBuilder loggingQueryBuilder = new SltrQueryBuilder("logged_featureset", "movie_features");
            loggingQueryBuilder.setStore(FEATURESTORE_NAME);
            loggingQueryBuilder.setParam("keywords", "rambo");

            LogQuerySearchRequest searchRequest = LogQuerySearchRequest.getRequestForQuery(termsQueryBuilder, loggingQueryBuilder);

            try {
                System.out.println(searchRequest.toJson());
            } catch (IOException e) {
                Assert.fail(e.getMessage());
            }

            SltrResponse response = client.search(INDEX, searchRequest);
            List<TmdbMovie> movies = response.getHits().asClass(TmdbMovie.class);

            assertEquals(movies.size(), 3);

            for (int i = 0; i < movies.size(); i++) {
                TmdbMovie movie = movies.get(i);

                assertTrue(ids.contains(movie.getId()));

                Fields fields = movie.getFields();
                List<Float> values = fields.getValues();

                assertEquals(values, expectedValues.get(i));
            }
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @After
    public void tearDown() {
        try (LearnToRankClient client = ClientUtils.getClient()) {
            client.dropFeatureStore(FEATURESTORE_NAME);
            assertFalse(client.featureStoreExists(FEATURESTORE_NAME));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

}
