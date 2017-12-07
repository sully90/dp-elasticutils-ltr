package com.github.onsdigital.elasticutils.ml.client.response.features.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.onsdigital.elasticutils.ml.util.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author sullid (David Sullivan) on 23/11/2017
 * @project dp-elasticutils-ltr
 *
 * Simple class to represent a FeatureSet in Elasticsearch LTR
 */
public class FeatureSet {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    private LinkedList<Feature> featureList;

    private FeatureSet() {
        this.featureList = new LinkedList<>();
    }

    public FeatureSet(String name) {
        this();
        this.name = name;
    }

    public FeatureSet(String name, LinkedList<Feature> featureList) {
        this.name = name;
        this.featureList = featureList;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getName() {
        return name;
    }

    @JsonProperty("features")
    public List<Feature> getFeatureList() {
        return featureList;
    }

    @JsonIgnore
    public String toJson() throws JsonProcessingException {
        return JsonUtils.toJson(this, true);
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof FeatureSet) {
            FeatureSet otherFeatureSet = (FeatureSet) other;
            return (otherFeatureSet.getName().equals(this.getName()));
        }
        return false;
    }

    @JsonIgnore
    public void dump(File parentDirectory) throws IOException {
        // Create a directory containing feature json files

        String featureSetDirectoryName = parentDirectory.getAbsolutePath() + "/" + this.getName();
        File featureSetDirectory = new File(featureSetDirectoryName);

        if (!featureSetDirectory.isDirectory()) {
            featureSetDirectory.mkdir();
        }

        for (Feature feature : this.getFeatureList()) {
            String featureFilename = featureSetDirectory.getAbsolutePath() + "/" + feature.getName() + ".json";
            File featureFile = new File(featureFilename);
            feature.writeJsonToFile(featureFile);
        }
    }

    public static FeatureSet readFromDirectory(File directory) throws IOException {
        if (!directory.isDirectory()) {
            throw new IOException("Must supply directory containing json (feature) files.");
        }
        String name = directory.getName();

        LinkedList<Feature> features = new LinkedList<>();

        File[] files = directory.listFiles(File::isFile);
        for (File file : files) {
            Feature feature = Feature.fromJsonFile(file);
            features.add(feature);
        }

        return new FeatureSet(name, features);
    }
}
