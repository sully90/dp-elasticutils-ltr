package com.github.onsdigital.elasticutils.ml.models;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class Genre {

    private int id;
    private String name;

    private Genre() {
        // For Jackson
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
