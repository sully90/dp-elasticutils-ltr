package com.github.onsdigital.elasticutils.ml.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.IOException;

/**
 * @author sullid (David Sullivan) on 06/12/2017
 * @project dp-elasticutils-ltr
 */
public interface JsonSerializable {

    @JsonIgnore
    String toJson() throws IOException;

}
