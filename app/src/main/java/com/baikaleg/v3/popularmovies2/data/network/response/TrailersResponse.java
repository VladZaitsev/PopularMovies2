package com.baikaleg.v3.popularmovies2.data.network.response;

import com.baikaleg.v3.popularmovies2.data.model.Trailer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersResponse {

    @SerializedName("results")
    @Expose
    private final List<Trailer> reviews = null;

    public List<Trailer> getTrailers() {
        return reviews;
    }
}
