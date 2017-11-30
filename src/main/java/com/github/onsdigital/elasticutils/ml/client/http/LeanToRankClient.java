package com.github.onsdigital.elasticutils.ml.client.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.onsdigital.elasticutils.client.http.SimpleRestClient;
import com.github.onsdigital.elasticutils.ml.client.http.response.LearnToRankHits;
import com.github.onsdigital.elasticutils.ml.client.http.response.LearnToRankResponse;
import com.github.onsdigital.elasticutils.ml.features.FeatureSet;
import com.github.onsdigital.elasticutils.ml.util.LearnToRankHelper;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.common.CheckedConsumer;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.xpack.common.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author sullid (David Sullivan) on 30/11/2017
 * @project dp-elasticutils-ltr
 */
public class LeanToRankClient extends SimpleRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeanToRankClient.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String LTR_INDEX = "_ltr";

    public LeanToRankClient(RestClientBuilder restClientBuilder) {
        super(restClientBuilder);
    }

    protected LeanToRankClient(RestClientBuilder restClientBuilder, List<NamedXContentRegistry.Entry> namedXContentEntries) {
        super(restClientBuilder, namedXContentEntries);
    }

    protected LeanToRankClient(RestClient restClient, CheckedConsumer<RestClient, IOException> doClose, List<NamedXContentRegistry.Entry> namedXContentEntries) {
        super(restClient, doClose, namedXContentEntries);
    }

    private Response get(String endPoint, Map<String, String> params) throws IOException {
        return super.getLowLevelClient().performRequest(HttpMethod.GET.method(), endPoint, params);
    }

    public Response listFeatureSets() throws IOException {
        String apiEndPoint = endpoint(LearnToRankEndPoint.FEATURESET);
        Map<String, String> params = Collections.EMPTY_MAP;

        return this.get(apiEndPoint, params);
    }

    // UTIL //

    static String endpoint(String[] indices, String[] types, String endpoint) {
        return endpoint(String.join(",", indices), String.join(",", types), endpoint);
    }

    static String endpoint(LearnToRankEndPoint... learnToRankEndPoints) {
        String[] endPoints = new String[learnToRankEndPoints.length];

        for (int i = 0; i < learnToRankEndPoints.length; i++) {
            endPoints[i] = learnToRankEndPoints[i].getEndPoint();
        }

        return endpoint(endPoints);
    }

    /**
     * Utility method to build request's endpoint.
     */
    static String endpoint(String... parts) {
        StringJoiner joiner = new StringJoiner("/", "/", "");
        // Add the _ltr endpoint
        joiner.add(LTR_INDEX);
        for (String part : parts) {
            if (Strings.hasLength(part)) {
                joiner.add(part);
            }
        }
        return joiner.toString();
    }

    enum LearnToRankEndPoint {
        FEATURESET("_featureset");

        private String endPoint;

        LearnToRankEndPoint(String endPoint) {
            this.endPoint = endPoint;
        }

        public String getEndPoint() {
            return endPoint;
        }
    }

    public static void main(String[] args) {
        try (LeanToRankClient client = LearnToRankHelper.getLTRClient("localhost")) {

            Response response = client.listFeatureSets();
            String content = EntityUtils.toString(response.getEntity());
            System.out.println(content);

            LearnToRankResponse learnToRankResponse = LeanToRankClient.MAPPER.readValue(content, LearnToRankResponse.class);
            LearnToRankHits hits = learnToRankResponse.getHits();
            FeatureSet featureSet = hits.getHits().get(0).getFeatureSet();
            System.out.println(featureSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
