package com.github.onsdigital.elasticutils.ml.query;

import org.apache.lucene.search.Query;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.QueryShardContext;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author sullid (David Sullivan) on 06/12/2017
 * @project dp-elasticutils-ltr
 */
public class SltrQueryBuilder extends AbstractQueryBuilder<SltrQueryBuilder> {
    public static final String NAME = "sltr";

    private static final String DEFAULT_STORE = "";

    private static final ParseField NAME_FIELD = new ParseField("_name");
    private static final ParseField STORE_FIELD = new ParseField("store");
    private static final ParseField FEATURESET_FIELD = new ParseField("featureset");
    private static final ParseField PARAM_FIELD = new ParseField("params");

    private final String name;
    private final String featureset;
    private Map<String, Object> params;

    private String store;

    public SltrQueryBuilder(String name, String featureset) {
        this(name, DEFAULT_STORE, featureset, new LinkedHashMap<>());
    }

    public SltrQueryBuilder(String name, String store, String featureset) {
        this(name, store, featureset, new LinkedHashMap<>());
    }

    public SltrQueryBuilder(String name, String store, String featureset, Map<String, Object> params) {
        this.name = name;
        this.store = store;
        this.featureset = featureset;
        this.params = params;
    }

    public String getSltrQueryName() {
        return this.name;
    }

    public void setParam(String key, Object value) {
        this.params.put(key, value);
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    @Override
    protected void doWriteTo(StreamOutput streamOutput) throws IOException {
        streamOutput.writeString(this.name);
        if (null != this.store && !this.store.isEmpty()) {
            streamOutput.writeString(this.store);
        }
        streamOutput.writeString(this.featureset);
        streamOutput.writeGenericValue(this.params);
    }

    @Override
    protected void doXContent(XContentBuilder xContentBuilder, Params params) throws IOException {
        xContentBuilder.startObject(NAME);
        xContentBuilder.field(NAME_FIELD.getPreferredName(), this.name);
        if (null != this.store && !this.store.isEmpty()) {
            xContentBuilder.field(STORE_FIELD.getPreferredName(), this.store);
        }
        xContentBuilder.field(FEATURESET_FIELD.getPreferredName(), this.featureset);
        xContentBuilder.field(PARAM_FIELD.getPreferredName(), this.params);
        xContentBuilder.endObject();
    }

    @Override
    protected Query doToQuery(QueryShardContext queryShardContext) throws IOException {
        return null;
    }

    @Override
    protected boolean doEquals(SltrQueryBuilder loggingQueryBuilder) {
        boolean equals = Objects.equals(this.name, loggingQueryBuilder.name) &&
                Objects.equals(this.featureset, loggingQueryBuilder.featureset) &&
                Objects.equals(this.params, loggingQueryBuilder.params);
        if (null != this.store && !this.store.isEmpty()) {
            return equals && this.store.equals(loggingQueryBuilder.getStore());
        }
        return equals;
    }

    @Override
    protected int doHashCode() {
        if (null != this.store && !this.store.isEmpty()) {
            return Objects.hash(this.name, this.store, this.featureset, this.params);
        }
        return Objects.hash(this.name, this.featureset, this.params);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }
}
