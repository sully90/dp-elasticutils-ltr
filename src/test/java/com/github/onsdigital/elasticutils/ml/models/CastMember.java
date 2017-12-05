package com.github.onsdigital.elasticutils.ml.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class CastMember {

    private int order;
    private int id;
    @JsonProperty("cast_id")
    private int castId;
    @JsonProperty("credit_id")
    private String creditId;
    private String name;
    @JsonProperty("profile_path")
    private String profilePath;
    private String character;

    private CastMember() {
        // For Jackson
    }

    public int getOrder() {
        return order;
    }

    public int getId() {
        return id;
    }

    public int getCastId() {
        return castId;
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

    public String getCharacter() {
        return character;
    }
}
