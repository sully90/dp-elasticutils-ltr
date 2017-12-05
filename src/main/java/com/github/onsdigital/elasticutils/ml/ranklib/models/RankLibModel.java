package com.github.onsdigital.elasticutils.ml.ranklib.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class RankLibModel {

    private String name;
    private Model model;

    public RankLibModel(String name, String type, String definition) {
        this.name = name;
        this.model = new Model(type, definition);
    }

    public String getName() {
        return name;
    }

    public Model getModel() {
        return model;
    }

    private class Model {
        private String type;
        private String definition;

        public Model(String type, String definition) {
            this.type = type;
            this.definition = definition;
        }
    }

}
