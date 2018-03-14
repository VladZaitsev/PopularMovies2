package com.baikaleg.v3.popularmovies2.data.network;

import com.baikaleg.v3.popularmovies2.BuildConfig;
import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.data.network.response.MoviesResponse;
import com.baikaleg.v3.popularmovies2.data.network.response.ReviewsResponse;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieService {

    @GET("popular?api_key=" + BuildConfig.API_KEY)
    Flowable<MoviesResponse> getPopularMovies();

    @GET("top_rated?api_key=" + BuildConfig.API_KEY)
    Flowable<MoviesResponse> getTopRatedMovies();

    @GET("{id}?api_key=" + BuildConfig.API_KEY)
    Observable<Movie> getMovie(@Path("id") int movieId);

    @GET("{id}/reviews?api_key=" + BuildConfig.API_KEY)
    Flowable<ReviewsResponse> getMovieReviews(@Path("id") int movieId);

   // @GET("{id}/videos?api_key=" + BuildConfig.API_KEY)
  //  Observable<ReviewsResponse> getVideos(@Path("id") int movieId);
}
