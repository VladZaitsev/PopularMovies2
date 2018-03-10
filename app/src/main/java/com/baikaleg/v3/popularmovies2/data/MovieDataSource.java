package com.baikaleg.v3.popularmovies2.data;

import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.ui.movies.MoviesFilterType;
import com.google.common.base.Optional;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface MovieDataSource {

    interface LoadMoviesCallback {

        void onMoviesLoaded(List<Movie> movies);

        void onDataNotAvailable();
    }

    interface GetMovieCallback {

        void onMovieLoaded(Movie movie);

        void onDataNotAvailable();
    }

   Observable<List<Movie>> getMovies(MoviesFilterType type);

    Flowable<Optional<Movie>> getMovie( int id);

    void markMovieAsFavorite(int id, String title, boolean favorite);
}
