package com.baikaleg.v3.popularmovies2.ui.movies;

/**
 * Defines the navigation actions that can be called from a list item in the movie list.
 */
public interface MovieItemNavigator {

    void openMovieDetails(int id, String title);
}
