package com.baikaleg.v3.popularmovies2.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Trailer extends BaseObservable {
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("type")
    @Expose
    private String type;


    private String ordinalName;

    public Trailer(String key, String name, String site, String size, String type) {
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
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

    @Bindable
    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
