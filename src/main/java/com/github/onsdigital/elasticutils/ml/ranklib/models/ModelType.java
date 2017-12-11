package com.github.onsdigital.elasticutils.ml.ranklib.models;

/**
 * @author sullid (David Sullivan) on 11/12/2017
 * @project dp-elasticutils-ltr
 */
public enum ModelType {
    RANKLIB("ranklib"),
    XGBOOSY("xgboost+json"),
    LINEAR("linear");

    private static final String PREFIX = "model";

    private String modelType;

    ModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getModelType() {
        return String.format("%s/%s", PREFIX, this.modelType);
    }
}
