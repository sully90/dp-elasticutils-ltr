package com.github.onsdigital.elasticutils.ml.ranklib.models;

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

}
