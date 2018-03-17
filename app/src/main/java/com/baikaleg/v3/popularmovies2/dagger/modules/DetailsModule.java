package com.baikaleg.v3.popularmovies2.dagger.modules;

import android.databinding.BaseObservable;

import com.baikaleg.v3.popularmovies2.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.popularmovies2.dagger.scopes.FragmentScoped;
import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.ui.details.DetailsActivity;
import com.baikaleg.v3.popularmovies2.ui.details.DetailsFragment;
import com.baikaleg.v3.popularmovies2.ui.details.DetailsViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public interface DetailsModule {

    @Provides
    @ActivityScoped
    static Movie provideMovie(DetailsActivity activity) {
        return activity.getIntent().getParcelableExtra(DetailsActivity.EXTRA_MOVIE);
    }

    @FragmentScoped
    @ContributesAndroidInjector
    DetailsFragment moviesDetailsFragment();

    @ActivityScoped
    @Binds
    BaseObservable moviesViewModel(DetailsViewModel viewModel);
} 