package com.github.onsdigital.elasticutils.ml.features;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * @author sullid (David Sullivan) on 23/11/2017
 * @project dp-elasticutils-ltr
 *
 * Simple feature for Elasticsearch LTR
 */
public class Feature {

    private String name;
    private List<String> params;
    private String templateLanguage;
    private Map<String, Object> template;

    @JsonIgnore
    private static final String DEFAULT_TEMPLATING_LANGUAGE = "mustache";

    public Feature (String name, List<String> params, Map<String, Object> template) {
        this(name, params, DEFAULT_TEMPLATING_LANGUAGE, template);
    }

    public Feature(String name, List<String> params, String templateLanguage, Map<String, Object> template) {
        this.name = name;
        this.params = params;
        this.templateLanguage = templateLanguage;
        this.template = template;
    }

    private Feature() {
        // For Jackson
    }

    public String getName() {
        return name;
    }

    public List<String> getParams() {
        return params;
    }

    @JsonProperty("template_language")
    public String getTemplateLanguage() {
        return templateLanguage;
    }

    public Map<String, Object> getTemplate() {
        return template;
    }
}
