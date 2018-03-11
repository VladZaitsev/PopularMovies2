package com.baikaleg.v3.popularmovies2.data;

import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.ui.movies.MoviesFilterType;

import java.util.List;

import io.reactivex.Observable;

public interface MovieDataSource {

    Observable<List<Movie>> getMovies(MoviesFilterType type);

    Observable<Movie> getMovie(int id);

    void markMovieAsFavorite(int id, String title, String posterPath, boolean favorite);
}
