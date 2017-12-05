package com.github.onsdigital.elasticutils.ml.client.response.sltr.models;

/**
 * @author sullid (David Sullivan) on 05/12/2017
 * @project dp-elasticutils-ltr
 */
public class LogEntry {

    private String name;
    private float value;

    public LogEntry(String name, float value) {
        this.name = name;
        this.value = value;
    }

    private LogEntry() {
        // For Jackson
    }

    public String getName() {
        return name;
    }

    public float getValue() {
        return value;
    }
}
