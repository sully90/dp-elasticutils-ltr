package com.github.onsdigital.elasticutils.ml;

import com.github.onsdigital.elasticutils.ml.client.http.LearnToRankClient;
import com.github.onsdigital.elasticutils.ml.client.http.response.sltr.SltrResponse;
import com.github.onsdigital.elasticutils.ml.client.http.response.sltr.models.Fields;
import com.github.onsdigital.elasticutils.ml.client.http.response.sltr.models.LogEntry;
import com.github.onsdigital.elasticutils.ml.models.TmdbMovie;
import com.github.onsdigital.elasticutils.ml.requests.LoggingQuery;
import com.github.onsdigital.elasticutils.ml.requests.models.SltrQuery;
import com.github.onsdigital.elasticutils.ml.util.LearnToRankHelper;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

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
            "                        \"featureset\": \"more_movie_features\",\n" +
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

    public static QueryBuilder getQuery() {
        QueryBuilder filterQuery = QueryBuilders.termsQuery("_id", "7555", "1370", "1369");

        return QueryBuilders.boolQuery().filter(filterQuery);
    }

    @Test
    public void test() {
        try (LearnToRankClient client = getClient()) {

            SltrResponse response = client.sltr(INDEX, QUERY);
            List<TmdbMovie> movies = response.getHits().asClass(TmdbMovie.class);
            Fields fields = movies.get(0).getFields();
            LogEntry entry = fields.getLtrLogList().get(0).get("log_entry").get(0);
            System.out.println(entry.getName() + " : " + entry.getValue());

            SltrQuery sltrQuery = new SltrQuery("logged_featureset", "more_movie_features");
            sltrQuery.setParam("keywords", "rambo");

            QueryBuilder filterQuery = getQuery();
            System.out.println(filterQuery.getName());

            LoggingQuery loggingQuery = new LoggingQuery(filterQuery, sltrQuery);
            System.out.println(loggingQuery.toJson());

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

}
