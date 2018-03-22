package com.baikaleg.v3.popularmovies2.data.network;

import com.baikaleg.v3.popularmovies2.BuildConfig;
import com.baikaleg.v3.popularmovies2.data.network.response.MoviesResponse;
import com.baikaleg.v3.popularmovies2.data.network.response.ReviewsResponse;
import com.baikaleg.v3.popularmovies2.data.network.response.TrailersResponse;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface MovieService {

    @GET("popular?api_key=" + BuildConfig.API_KEY)
    Observable<MoviesResponse> getPopularMovies();

    @GET("top_rated?api_key=" + BuildConfig.API_KEY)
    Observable<MoviesResponse> getTopRatedMovies();

    @GET("{id}/reviews?api_key=" + BuildConfig.API_KEY)
    Flowable<ReviewsResponse> getMovieReviews(@Path("id") int movieId);

    @GET("{id}/videos?api_key=" + BuildConfig.API_KEY)
    Observable<TrailersResponse> getTrailers(@Path("id") int movieId);

    @GET
    Observable<Response<ResponseBody>> downloadImage(@Url String fileUrl);
}
