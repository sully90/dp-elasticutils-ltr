package com.github.onsdigital.elasticutils.ml.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class Director {

    private int id;
    private String department;
    @JsonProperty("credit_id")
    private String creditId;
    private String name;
    @JsonProperty("profile_path")
    private String profilePath;
    private String job;

    private Director() {
        // For Jackson
    }

    public int getId() {
        return id;
    }

    public String getDepartment() {
        return department;
    }

    public String getCreditId() {
        return creditId;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getJob() {
        return job;
    }
}
