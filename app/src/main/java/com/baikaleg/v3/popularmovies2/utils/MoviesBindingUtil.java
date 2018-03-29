package com.baikaleg.v3.popularmovies2.utils;

import android.databinding.BindingAdapter;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baikaleg.v3.popularmovies2.R;
import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.data.model.Review;
import com.baikaleg.v3.popularmovies2.data.model.Trailer;
import com.baikaleg.v3.popularmovies2.ui.details.reviews.ReviewPagerAdapter;
import com.baikaleg.v3.popularmovies2.ui.details.trailers.TrailerViewAdapter;
import com.baikaleg.v3.popularmovies2.ui.movies.MoviesViewModel;
import com.baikaleg.v3.popularmovies2.ui.movies.adapter.MoviesViewAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Contains {@link BindingAdapter}s for assigning a list of {@link Movie}, {@link Review},
 * {@link Trailer} items to a ViewGroup.
 */
public class MoviesBindingUtil {
    /**
     * Prevent instantiation
     */
    private MoviesBindingUtil() {
    }

    @BindingAdapter({"src:image"})
    public static void setImageUrl(@NonNull ImageView imageView, @NonNull String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (url.contains(Environment.getExternalStorageDirectory().toString())) {
            Picasso.with(imageView.getContext())
                    .load(new File(url))
                    .fit()
                    .placeholder(imageView.getContext().getResources().getDrawable(R.drawable.ic_image))
                    .into(imageView);
        } else {
            Picasso.with(imageView.getContext())
                    .load(url)
                    .fit()
                    .placeholder(imageView.getContext().getResources().getDrawable(R.drawable.ic_image))
                    .into(imageView);
        }

    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:movies")
    public static void setMovies(RecyclerView recyclerView, List<Movie> movies) {
        MoviesViewAdapter adapter = (MoviesViewAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.refreshAdapter(movies);
        }
    }

    @BindingAdapter("app:onRefresh")
    public static void setSwipeRefreshLayoutOnRefreshListener(SwipeRefreshLayout view, final MoviesViewModel viewModel) {
        view.setOnRefreshListener(() -> viewModel.loadMovies(true,true));
    }


    @SuppressWarnings("unchecked")
    @BindingAdapter("app:reviews")
    public static void setReviews(ViewPager pager, List<Review> reviews) {
        ReviewPagerAdapter adapter = (ReviewPagerAdapter) pager.getAdapter();
        if (adapter != null) {
            adapter.refreshAdapter(reviews);
        }
    }

    @BindingAdapter("app:pager_height")
    public static void setReviewsPagerHeight(ViewPager view, float height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) height;
        view.setLayoutParams(layoutParams);
    }


    @SuppressWarnings("unchecked")
    @BindingAdapter("app:trailers")
    public static void setTrailers(RecyclerView recyclerView, List<Trailer> trailers) {
        TrailerViewAdapter adapter = (TrailerViewAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.refreshAdapter(trailers);
        }
    }
}
