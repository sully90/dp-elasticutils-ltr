package com.github.onsdigital.elasticutils.ml.ranklib.models;

import com.github.onsdigital.elasticutils.ml.client.http.LearnToRankClient;
import com.github.onsdigital.elasticutils.ml.util.JsonUtils;
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

    public RankLibModel(String name, ModelType type, String definition) {
        this.name = name;
        this.model = new Model(type, definition);
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

    public static RankLibModel fromFile(String name, ModelType type, String filename) throws IOException {
        String content = getDefinition(filename);
        return new RankLibModel(name, type, content);
    }

    private class Model {

        private String type;
        private String definition;

        public Model(ModelType type, String definition) {
            this.type = type.getModelType();
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
            String content = RankLibModel.getDefinition(filename);
            RankLibModel model = new RankLibModel("test_6", ModelType.RANKLIB, content);

            String json = JsonUtils.toJson(model);
            System.out.println(json);

            String featureset = "movie_features";

            Response response = client.createModel(featureset, model);
            String entity = EntityUtils.toString(response.getEntity());

            System.out.println(entity);

            Response getResponse = client.getModel(model.getName());
            String getEntity = EntityUtils.toString(getResponse.getEntity());
            System.out.println(getEntity);

//            client.deleteModel(model.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
