package com.baikaleg.v3.popularmovies2.network;

import com.baikaleg.v3.popularmovies2.BuildConfig;
import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.data.model.MoviesResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieService {

    @GET("popular/?api_key=" + BuildConfig.API_KEY)
    Observable<MoviesResponse> getPopularMovies();

    @GET("top_rated/?api_key=" + BuildConfig.API_KEY)
    Observable<MoviesResponse> getTopRatedMovies();

    @GET("movie/{id}/?api_key=" + BuildConfig.API_KEY)
    Observable<Movie> getMovie(@Path("id") int movieId);
}
