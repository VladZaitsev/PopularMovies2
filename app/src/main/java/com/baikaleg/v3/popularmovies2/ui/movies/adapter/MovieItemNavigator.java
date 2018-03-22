package com.baikaleg.v3.popularmovies2.ui.movies.adapter;

import com.baikaleg.v3.popularmovies2.data.model.Movie;

/**
 * Defines the navigation actions that can be called from a list item in the movie list.
 */
public interface MovieItemNavigator {

    void openMovieDetails(Movie movie);
}
