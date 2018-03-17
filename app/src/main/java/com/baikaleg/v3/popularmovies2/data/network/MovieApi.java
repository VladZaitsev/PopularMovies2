package com.baikaleg.v3.popularmovies2.data.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.baikaleg.v3.popularmovies2.R;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
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
    public MovieService createService(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MovieService.class);
    }
}