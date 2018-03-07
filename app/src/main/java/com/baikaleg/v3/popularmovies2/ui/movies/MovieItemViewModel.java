package com.baikaleg.v3.popularmovies2.ui.movies;

import android.databinding.Bindable;
import android.support.annotation.Nullable;

import com.baikaleg.v3.popularmovies2.MovieViewModel;

import java.lang.ref.WeakReference;

public class MovieItemViewModel extends MovieViewModel {
    private int height, width;

    @Nullable
    private WeakReference<MovieItemNavigator> navigator;

    public MovieItemViewModel(int height, int width) {
        super();
        this.height = height;
        this.width = width;
    }

    public void setNavigator(MovieItemNavigator navigator) {
        this.navigator = new WeakReference<>(navigator);
    }

    public void movieClicked() {
        int movieId = getMovieId();
        if (movieId == 0) {
            return;
        }
        if (navigator != null && navigator.get() != null) {
            navigator.get().openMovieDetails(movieId);
        }
    }

    @Bindable
    public int getHeight() {
        return height;
    }

    @Bindable
    public int getWight() {
        return width;
    }
}
