package com.baikaleg.v3.popularmovies2.dagger;

import android.app.Application;
import android.content.Context;

import com.baikaleg.v3.popularmovies2.dagger.modules.MoviesModule;
import com.baikaleg.v3.popularmovies2.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.popularmovies2.ui.movies.MoviesActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Module(includes = AndroidSupportInjectionModule.class)
public interface AppModule {
    @Binds
    Context bindContext(Application application);

    @ActivityScoped
    @ContributesAndroidInjector(modules = {MoviesModule.class})
    MoviesActivity moviesActivityInjector();


} 