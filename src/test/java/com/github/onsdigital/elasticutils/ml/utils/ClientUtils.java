package com.github.onsdigital.elasticutils.ml.utils;

import com.github.onsdigital.elasticutils.ml.client.http.LearnToRankClient;
import com.github.onsdigital.elasticutils.ml.util.LearnToRankHelper;

/**
 * Utility class for tests
 * @author sullid (David Sullivan) on 12/12/2017
 * @project dp-elasticutils-ltr
 */
public class ClientUtils {

    private static final String HOSTNAME = "localhost";

    public static LearnToRankClient getClient() {
        return LearnToRankHelper.getLTRClient(HOSTNAME);
    }

}
