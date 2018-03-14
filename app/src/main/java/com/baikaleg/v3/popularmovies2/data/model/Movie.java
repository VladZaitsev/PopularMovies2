package com.baikaleg.v3.popularmovies2.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("id")
    @Expose
    private  int id;
    @SerializedName("title")
    @Expose
    private  String title;
    @SerializedName("poster_path")
    @Expose
    private  String posterPath;
    @SerializedName("overview")
    @Expose
    private  String overview;
    @SerializedName("release_date")
    @Expose
    private  String releaseDate;
    @SerializedName("vote_average")
    @Expose
    private  double voteAverage;

    private boolean isFavorite;


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
