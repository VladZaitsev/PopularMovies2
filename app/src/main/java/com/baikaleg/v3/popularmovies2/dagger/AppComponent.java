package com.baikaleg.v3.popularmovies2.dagger;

import android.app.Application;

import com.baikaleg.v3.popularmovies2.data.Repository;
import com.baikaleg.v3.popularmovies2.data.network.MovieApi;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent extends AndroidInjector<DaggerApplication> {

    @Override
    void inject(DaggerApplication instance);

    void inject(MovieApi movieApi);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    Repository getRepository();
} 