package com.github.onsdigital.elasticutils.ml.ranklib.models;

import com.github.onsdigital.elasticutils.ml.client.http.LearnToRankClient;
import com.github.onsdigital.elasticutils.ml.util.LearnToRankHelper;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class RankLibModel {

    private String name;
    private Model model;

    public RankLibModel(String name, String definition) {
        this.name = name;
        this.model = new Model(definition);
    }

    private RankLibModel() {
        // For Jenkins
    }

    public String getName() {
        return name;
    }

    public Model getModel() {
        return model;
    }

    public static String getDefinition(String filename) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        return content;
    }

    public static RankLibModel fromFile(String name, String filename) throws IOException {
        String content = getDefinition(filename);
        return new RankLibModel(name, content);
    }

    private class Model {
        private final String type = "model/ranklib";
        private String definition;

        public Model(String definition) {
            this.definition = definition;
        }

        private Model() {
            // For Jenkins
        }

        public String getType() {
            return type;
        }

        public String getDefinition() {
            return definition;
        }
    }

    public static void main(String[] args) {
        try (LearnToRankClient client = LearnToRankHelper.getLTRClient("localhost")) {
            String filename = "/Users/sullid/idea/elasticsearch-learning-to-rank/demo/model_java.txt";
            RankLibModel model = new RankLibModel("test_6", RankLibModel.getDefinition(filename));

            String featureset = "movie_features";

            Response response = client.uploadModel(featureset, model);
            String entity = EntityUtils.toString(response.getEntity());

            System.out.println(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
