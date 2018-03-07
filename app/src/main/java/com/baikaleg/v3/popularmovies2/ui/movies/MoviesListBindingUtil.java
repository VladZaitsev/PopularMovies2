package com.baikaleg.v3.popularmovies2.ui.movies;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.ui.movies.adapter.MoviesViewAdapter;

import java.util.List;

/**
 * Contains {@link BindingAdapter}s for the {@link Movie} list.
 */
public class MoviesListBindingUtil {
    @SuppressWarnings("unchecked")
    @BindingAdapter("app:items")
    public static void setItems(RecyclerView recyclerView, List<Movie> items) {
        MoviesViewAdapter adapter = (MoviesViewAdapter) recyclerView.getAdapter();
        if (adapter != null)
        {
            adapter.refreshAdapter(items);
        }
    }

    @BindingAdapter("app:onRefresh")
    public static void setSwipeRefreshLayoutOnRefreshListener(SwipeRefreshLayout view, final MoviesViewModel viewModel) {
        view.setOnRefreshListener(() -> viewModel.loadMovies(true));
    }
} 