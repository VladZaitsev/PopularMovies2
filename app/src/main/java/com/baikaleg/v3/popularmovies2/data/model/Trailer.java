package com.baikaleg.v3.popularmovies2.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Trailer  {

    @SerializedName("key")
    @Expose
    private final String key;
    @SerializedName("site")
    @Expose
    private final String site;
    @SerializedName("type")
    @Expose
    private final String type;

    public Trailer(String key, String site, String type) {
        this.key = key;
        this.site = site;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }
}
