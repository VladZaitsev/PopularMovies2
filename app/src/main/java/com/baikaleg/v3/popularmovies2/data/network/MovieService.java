package com.baikaleg.v3.popularmovies2.data.network;

import com.baikaleg.v3.popularmovies2.data.network.response.MoviesResponse;
import com.baikaleg.v3.popularmovies2.data.network.response.ReviewsResponse;
import com.baikaleg.v3.popularmovies2.data.network.response.TrailersResponse;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MovieService {

    @GET("{sort_by}")
    Observable<MoviesResponse> getMovies(@Path("sort_by") String sortBy, @Query("api_key") String apiKey);

    @GET("{id}/reviews")
    Flowable<ReviewsResponse> getMovieReviews(@Path("id") int movieId, @Query("api_key") String apiKey);

    @GET("{id}/videos")
    Observable<TrailersResponse> getTrailers(@Path("id") int movieId, @Query("api_key") String apiKey);

    @GET
    Observable<Response<ResponseBody>> downloadImage(@Url String fileUrl, @Query("api_key") String apiKey);
}
