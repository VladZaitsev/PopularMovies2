package com.baikaleg.v3.popularmovies2.dagger.modules;

import android.databinding.BaseObservable;

import com.baikaleg.v3.popularmovies2.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.popularmovies2.dagger.scopes.FragmentScoped;
import com.baikaleg.v3.popularmovies2.ui.movies.MoviesFragment;
import com.baikaleg.v3.popularmovies2.ui.movies.MoviesViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface MoviesModule {

    @FragmentScoped
    @ContributesAndroidInjector
    MoviesFragment moviesFragment();

    @ActivityScoped
    @Binds
    BaseObservable moviesViewModel(MoviesViewModel viewModel);
} 