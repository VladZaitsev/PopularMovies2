package com.baikaleg.v3.popularmovies2.data.network.response;

import com.baikaleg.v3.popularmovies2.data.model.Review;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResponse {

    @SerializedName("results")
    @Expose
    private final List<Review> reviews = null;

    public List<Review> getReviews() {
        return reviews;
    }
}