package com.baikaleg.v3.popularmovies2.utils;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baikaleg.v3.popularmovies2.BR;
import com.baikaleg.v3.popularmovies2.R;
import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.data.model.Review;
import com.baikaleg.v3.popularmovies2.data.model.Trailer;
import com.baikaleg.v3.popularmovies2.ui.details.adapter.ReviewPagerAdapter;
import com.baikaleg.v3.popularmovies2.ui.movies.MoviesViewModel;
import com.baikaleg.v3.popularmovies2.ui.movies.adapter.MoviesViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Contains {@link BindingAdapter}s for assigning a list of {@link Movie}, {@link Review},
 * {@link Trailer} items to a ViewGroup.
 */
public class MoviesBindingUtil {
    private static final String TAG = MoviesBindingUtil.class.getSimpleName();
    private static final String VIDEO_SOURCE = "YouTube";

    /**
     * Prevent instantiation
     */
    private MoviesBindingUtil() {
    }

    @BindingAdapter({"app:image", "app:image_height", "app:image_width"})
    public static void setImageUrl(@NonNull ImageView imageView, @NonNull String url, int height, int width) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Picasso.with(imageView.getContext())
                .load(imageView.getContext().getString(R.string.image_base_url) + url)
                .resize(width, height)
                .into(imageView);
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
        view.setOnRefreshListener(() -> viewModel.loadMovies(true));
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
    @BindingAdapter({"app:trailers", "app:layout"})
    public static void setTrailers(LinearLayout viewGroup, List<Trailer> trailers, int layoutId) {
        viewGroup.removeAllViews();
        if (layoutId == 0) {
            return;
        }
        if (trailers != null) {
            Context context = viewGroup.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < trailers.size(); i++) {
                Trailer trailer = trailers.get(i);
                ViewDataBinding binding = DataBindingUtil
                        .inflate(inflater, layoutId, viewGroup, false);
                binding.setVariable(BR.data, trailer);
                binding.getRoot().setOnClickListener(v -> {
                    if (trailer.getSite().equals(VIDEO_SOURCE)) {
                        Uri uri = Uri.parse(context.getString(R.string.youtube_base_url) + trailer.getKey());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    } else {
                        Log.i(TAG, context.getString(R.string.no_source));
                    }
                });
                viewGroup.addView(binding.getRoot());
            }
        }
    }
}
