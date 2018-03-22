package com.baikaleg.v3.popularmovies2.data;

import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.data.model.Review;
import com.baikaleg.v3.popularmovies2.data.model.Trailer;
import com.baikaleg.v3.popularmovies2.ui.movies.MoviesFilterType;

import java.util.List;

import io.reactivex.Observable;

public interface MovieDataSource {

    Observable<List<Movie>> getMovies(MoviesFilterType type);

    Observable<List<Review>> getReviews(int id);

    Observable<List<Trailer>> getTrailers(int id);

    void markMovieAsFavorite(Movie movie, boolean favorite);

    void refreshMovies();
}
