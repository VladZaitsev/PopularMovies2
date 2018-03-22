package com.baikaleg.v3.popularmovies2.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Trailer extends BaseObservable {
    @SerializedName("key")
    @Expose
    private final String key;
    @SerializedName("site")
    @Expose
    private final String site;
    @SerializedName("type")
    @Expose
    private final String type;


    private String ordinalName;

    public Trailer(String key,  String site, String type) {
        this.key = key;
        this.site = site;
        this.type = type;
    }

    public void setOrdinalName(String ordinalName) {
        this.ordinalName = ordinalName;
    }

    public String getOrdinalName() {
        return ordinalName;
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
