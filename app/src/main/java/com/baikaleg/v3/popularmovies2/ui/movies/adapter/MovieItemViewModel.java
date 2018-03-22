package com.baikaleg.v3.popularmovies2.ui.movies.adapter;

import android.support.annotation.Nullable;

import com.baikaleg.v3.popularmovies2.MovieViewModel;
import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.ui.movies.MovieItemNavigator;

import java.lang.ref.WeakReference;

public class MovieItemViewModel extends MovieViewModel {

    @Nullable
    private WeakReference<MovieItemNavigator> navigator;

    MovieItemViewModel() {
        super();
    }

    void setNavigator(@Nullable MovieItemNavigator navigator) {
        this.navigator = new WeakReference<>(navigator);
    }

    public void movieClicked() {
        Movie movie = movieObservable.get();
        if (movie == null) {
            return;
        }
        if (navigator != null && navigator.get() != null) {
            navigator.get().openMovieDetails(movie);
        }
    }

}
