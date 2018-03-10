package com.baikaleg.v3.popularmovies2.ui.details;

import com.baikaleg.v3.popularmovies2.MovieViewModel;
import com.baikaleg.v3.popularmovies2.data.Repository;

import javax.annotation.Nullable;

public class MovieDetailViewModel extends MovieViewModel {

    @Nullable
    private MovieDetailNavigator navigator;

    public MovieDetailViewModel(Repository repository) {
        super(repository);
    }

    public void setNavigator(MovieDetailNavigator navigator) {
        this.navigator = navigator;
    }

    public void onDestroy() {
        navigator = null;
    }

}
