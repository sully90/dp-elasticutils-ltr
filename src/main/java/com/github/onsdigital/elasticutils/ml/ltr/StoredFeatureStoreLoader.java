package com.github.onsdigital.elasticutils.ml.ltr;

import com.o19s.es.ltr.feature.store.FeatureStore;
import com.o19s.es.ltr.utils.FeatureStoreLoader;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;

/**
 * @author sullid (David Sullivan) on 11/01/2018
 * @project dp-elasticutils-ltr
 */
public class StoredFeatureStoreLoader implements FeatureStoreLoader {
    @Override
    public FeatureStore load(String storeName, Client client) {
        String api = String.format("/_ltr/%s", storeName);

        GetResponse getResponse = client.prepareGet()
                .setIndex(api)
                .get();

        if (getResponse.isExists()) {
            return new StoredFeatureStore(storeName);
        } else {
            return null;
        }
    }
}
