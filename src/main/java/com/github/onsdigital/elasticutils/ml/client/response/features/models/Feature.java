package com.github.onsdigital.elasticutils.ml.client.response.features.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.onsdigital.elasticutils.ml.util.LearnToRankHelper;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author sullid (David Sullivan) on 23/11/2017
 * @project dp-elasticutils-ltr
 *
 * Simple feature for Elasticsearch LTR
 */
public class Feature {

    @JsonIgnore
    public static final String[] DEFAULT_PARAMS;

    static {
        DEFAULT_PARAMS = new String[] {
                "keywords"
        };
    }

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

    public static Feature matchFeature(String name, String field) throws IOException {
        return matchFeature(name, field, DEFAULT_PARAMS);
    }

    public static Feature matchFeature(String name, String field, String[] params) throws IOException {
        // Example of a simple match field feature
        QueryBuilder qb = QueryBuilders.matchQuery(field, "{{keywords}}");
        Map<String, Object> template = LearnToRankHelper.templateMapFromQueryBuilder(qb);

        Feature feature = new Feature(name, Arrays.asList(params), template);
        return feature;
    }
}
