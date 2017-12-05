package com.github.onsdigital.elasticutils.ml.models;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class ProductionCompany {

    private String name;
    private int id;

    private ProductionCompany() {
        // For Jackson
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
