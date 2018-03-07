package com.baikaleg.v3.popularmovies2.data;

import com.baikaleg.v3.popularmovies2.data.model.Movie;

import java.util.List;

import io.reactivex.Observable;

public interface DataSource {

    Observable<List<Movie>> getFavoriteMovies();

    Observable<Movie> getFavoriteMovie();

    void addFavoriteMovie(Movie movie);

    void deleteFavoriteMovie(int id);
}
