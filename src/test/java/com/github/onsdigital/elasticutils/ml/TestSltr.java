package com.github.onsdigital.elasticutils.ml;

import com.github.onsdigital.elasticutils.ml.client.http.LearnToRankClient;
import com.github.onsdigital.elasticutils.ml.client.response.sltr.SltrResponse;
import com.github.onsdigital.elasticutils.ml.client.response.sltr.models.SltrDocument;
import com.github.onsdigital.elasticutils.ml.models.TmdbMovie;
import com.github.onsdigital.elasticutils.ml.query.SltrQueryBuilder;
import com.github.onsdigital.elasticutils.ml.ranklib.Exporter;
import com.github.onsdigital.elasticutils.ml.ranklib.models.Judgement;
import com.github.onsdigital.elasticutils.ml.requests.LogQuerySearchRequest;
import com.github.onsdigital.elasticutils.ml.requests.models.LogSpecs;
import com.github.onsdigital.elasticutils.ml.util.LearnToRankHelper;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class TestSltr {

    private static final String HOSTNAME = "localhost";
    private static final String INDEX = "tmdb";
    private static final String QUERY = "{\n" +
            "    \"query\": {\n" +
            "        \"bool\": {\n" +
            "            \"filter\": [\n" +
            "                {\n" +
            "                    \"terms\": {\n" +
            "                        \"_id\": [\"7555\", \"1370\", \"1369\"]\n" +
            "\n" +
            "                    }\n" +
            "                },\n" +
            "                {\n" +
            "                    \"sltr\": {\n" +
            "                        \"_name\": \"logged_featureset\",\n" +
            "                        \"featureset\": \"movie_features\",\n" +
            "                        \"params\": {\n" +
            "                            \"keywords\": \"rambo\"\n" +
            "                        }\n" +
            "                }}\n" +
            "\n" +
            "            ]\n" +
            "        }\n" +
            "    },\n" +
            "    \"ext\": {\n" +
            "        \"ltr_log\": {\n" +
            "            \"log_specs\": {\n" +
            "                \"name\": \"log_entry\",\n" +
            "                \"named_query\": \"logged_featureset\"\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}";

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

            TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("_id", "7555", "1370", "1369");

            SltrQueryBuilder loggingQueryBuilder = new SltrQueryBuilder("logged_featureset", "movie_features");
            loggingQueryBuilder.setParam("keywords", "rambo");

            LogSpecs logSpecs = new LogSpecs("log_entry", "logged_featureset");

            QueryBuilder qb = QueryBuilders.boolQuery()
                    .filter(termsQueryBuilder)
                    .filter(loggingQueryBuilder);

            LogQuerySearchRequest searchRequest = new LogQuerySearchRequest(qb, logSpecs);

            System.out.println(searchRequest.toJson());

            SltrResponse response = client.sltr(INDEX, searchRequest, 10);
            List<TmdbMovie> movies = response.getHits().asClass(TmdbMovie.class);
            List<Judgement> judgements = generateTestJudgements(movies.size());
            Map<Judgement, SltrDocument> queryFeatureMap = new HashMap<>();

            for (int i = 0; i < movies.size(); i++) {
                queryFeatureMap.put(judgements.get(i), movies.get(i));
            }

            String outPath = "/Users/sullid/idea/elasticsearch-learning-to-rank/demo";
            String fileName = "java_sample_judgements_wfeatures.txt";

            List<String> rankLibFormat = Exporter.toRankLibFormat(queryFeatureMap);

            System.out.println(rankLibFormat);

            Exporter.export(outPath + "/" + fileName, queryFeatureMap);

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

}
