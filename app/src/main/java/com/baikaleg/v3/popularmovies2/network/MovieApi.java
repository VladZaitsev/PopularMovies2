package com.baikaleg.v3.popularmovies2.network;


import android.content.Context;
import android.support.annotation.NonNull;

import com.baikaleg.v3.popularmovies2.R;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieApi {

    private final Context context;

    @Inject
    MovieApi(Context context) {
        this.context = context;
    }

    @NonNull
    public MovieService createService() {
        return new Retrofit.Builder()
                .baseUrl(context.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MovieService.class);
    }
}