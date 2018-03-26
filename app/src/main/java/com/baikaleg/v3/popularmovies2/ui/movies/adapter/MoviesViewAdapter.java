package com.baikaleg.v3.popularmovies2.ui.movies.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.baikaleg.v3.popularmovies2.R;
import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.databinding.ItemMoviesBinding;

import java.util.ArrayList;
import java.util.List;

public class MoviesViewAdapter extends RecyclerView.Adapter<MoviesViewHolder> {
    private final List<Movie> movies = new ArrayList<>();
    private MovieItemNavigator itemNavigator;
    private int viewHeight;
    private int viewWidth;
    private RecyclerView recyclerView;
    private int positionToScroll = 0;

    public MoviesViewAdapter(MovieItemNavigator itemNavigator, RecyclerView recyclerView) {
        this.itemNavigator = itemNavigator;
        this.recyclerView = recyclerView;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMoviesBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_movies, parent, false);
        binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(viewWidth, viewHeight));
        return new MoviesViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        Movie movie = movies.get(position);
        final MovieItemViewModel viewModel = new MovieItemViewModel();
        viewModel.setNavigator(itemNavigator);
        viewModel.setMovie(movie);
        holder.binding.setView(holder);
        holder.binding.setViewmodel(viewModel);
    }

    @Override
    public int getItemCount() {
        return this.movies.size();
    }

    public void refreshAdapter(@NonNull List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
        recyclerView.scrollToPosition(positionToScroll);
    }

    public void onDestroy() {
        itemNavigator = null;
    }

    public void setViewSize(int viewWidth, int viewHeight) {
        this.viewHeight = viewHeight;
        this.viewWidth = viewWidth;
    }

    public void setPositionToScroll(int positionToScroll) {
        this.positionToScroll = positionToScroll;
    }
}
