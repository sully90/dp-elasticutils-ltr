package com.github.onsdigital.elasticutils.ml;

import com.github.onsdigital.elasticutils.ml.client.http.LearnToRankClient;
import com.github.onsdigital.elasticutils.ml.client.response.sltr.SltrResponse;
import com.github.onsdigital.elasticutils.ml.client.response.sltr.models.Fields;
import com.github.onsdigital.elasticutils.ml.models.TmdbMovie;
import com.github.onsdigital.elasticutils.ml.query.SltrQueryBuilder;
import com.github.onsdigital.elasticutils.ml.ranklib.models.Judgement;
import com.github.onsdigital.elasticutils.ml.requests.LogQuerySearchRequest;
import com.github.onsdigital.elasticutils.ml.util.LearnToRankHelper;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class TestSltr {

    private static final String HOSTNAME = "localhost";
    private static final String INDEX = "tmdb";

    private static final List<Integer> ids = new ArrayList<Integer>() {{
        add(7555);
        add(1370);
        add(1369);
    }};
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

    public static LearnToRankClient getClient() {
        return LearnToRankHelper.getLTRClient(HOSTNAME);
    }

    public List<Judgement> generateTestJudgements(int num) {
        List<Judgement> judgements = new LinkedList<>();
        for (int i = 0; i < num; i++) {
            judgements.add(Judgement.randomJudgement(1, "test comment"));
        }

        return judgements;
    }

    @Test
    public void test() {
        try (LearnToRankClient client = getClient()) {

            QueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("_id", "7555", "1370", "1369");

            SltrQueryBuilder loggingQueryBuilder = new SltrQueryBuilder("logged_featureset", "movie_features");
            loggingQueryBuilder.setParam("keywords", "rambo");

            LogQuerySearchRequest searchRequest = LogQuerySearchRequest.getRequestForQuery(termsQueryBuilder, loggingQueryBuilder);

            SltrResponse response = client.sltr(INDEX, searchRequest);
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

}
