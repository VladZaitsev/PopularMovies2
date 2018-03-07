package com.baikaleg.v3.popularmovies2;

import com.baikaleg.v3.popularmovies2.dagger.AppComponent;
import com.baikaleg.v3.popularmovies2.dagger.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class App extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }

}
