package com.github.onsdigital.elasticutils.ml.ltr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.onsdigital.elasticutils.ml.client.http.LearnToRankClient;
import com.github.onsdigital.elasticutils.ml.client.response.features.LearnToRankGetResponse;
import com.github.onsdigital.elasticutils.ml.client.response.features.LearnToRankHit;
import com.github.onsdigital.elasticutils.ml.client.response.features.LearnToRankListResponse;
import com.github.onsdigital.elasticutils.ml.requests.FeatureSetRequest;
import com.github.onsdigital.elasticutils.ml.util.LearnToRankHelper;
import com.o19s.es.ltr.feature.Feature;
import com.o19s.es.ltr.feature.FeatureSet;
import com.o19s.es.ltr.feature.store.CompiledLtrModel;
import com.o19s.es.ltr.feature.store.FeatureStore;
import com.o19s.es.ltr.feature.store.StoredFeature;
import com.o19s.es.ltr.feature.store.StoredFeatureSet;
import com.o19s.es.ltr.ranker.NullRanker;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author sullid (David Sullivan) on 11/01/2018
 * @project dp-elasticutils-ltr
 */
public class StoredFeatureStore implements FeatureStore {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String name;
    private final String elasticHost;

    public StoredFeatureStore(String name, String elasticHost) {
        this.name = name;
        this.elasticHost = elasticHost;
    }

    @Override
    public String getStoreName() {
        return this.name;
    }

    @Override
    public Feature load(String feature) throws IOException {
        try (LearnToRankClient client = LearnToRankHelper.getLTRClient(this.elasticHost)) {
            LearnToRankListResponse<FeatureSetRequest> response = client.listFeatureSets(this.name);

            for (LearnToRankHit<FeatureSetRequest> hit : response.getHits().getHits()) {
                FeatureSetRequest request = hit.getSource();
                StoredFeatureSet storedFeatureSet = fromRequest(request);
                if (storedFeatureSet.hasFeature(feature)) {
                    return storedFeatureSet.feature(feature);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FeatureSet loadSet(String featureSet) throws IOException {
        // Check if feature set exists
        try (LearnToRankClient learnToRankClient = LearnToRankHelper.getLTRClient(this.elasticHost)) {
            if (learnToRankClient.featureSetExists(this.name, featureSet)) {
                LearnToRankGetResponse<FeatureSetRequest> response = learnToRankClient.getFeatureSet(this.name, featureSet);
                FeatureSetRequest featureSetRequest = response.getSource();

                StoredFeatureSet storedFeatureSet = fromRequest(featureSetRequest);
                return storedFeatureSet;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private StoredFeatureSet fromRequest(FeatureSetRequest request) throws JsonProcessingException {
        List<StoredFeature> features = new LinkedList<>();
        List<com.github.onsdigital.elasticutils.ml.client.response.features.models.Feature> featureList = request.getFeatureSet().getFeatureList();
        for (com.github.onsdigital.elasticutils.ml.client.response.features.models.Feature feature : featureList) {
            String templateString = MAPPER.writeValueAsString(feature.getTemplate());
            StoredFeature storedFeature = new StoredFeature(feature.getName(), feature.getParams(), feature.getTemplateLanguage(), templateString);
            features.add(storedFeature);
        }

        StoredFeatureSet storedFeatureSet = new StoredFeatureSet(request.getName(), features);
        return storedFeatureSet;
    }

    @Override
    public CompiledLtrModel loadModel(String modelName) throws IOException {
        FeatureSet storedFeatureSet = this.loadSet(this.name);
        return new CompiledLtrModel(modelName, storedFeatureSet, new NullRanker(1000));
    }
}
