package com.baikaleg.v3.popularmovies2.ui.details.trailers;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.baikaleg.v3.popularmovies2.R;
import com.baikaleg.v3.popularmovies2.data.model.Trailer;
import com.baikaleg.v3.popularmovies2.databinding.ItemTrailerBinding;

import java.util.ArrayList;
import java.util.List;

public class TrailerViewAdapter extends RecyclerView.Adapter<TrailerViewHolder> {
    private final List<Trailer> trailers = new ArrayList<>();
    private TrailersItemNavigator navigator;
    private int viewHeight;
    private int viewWidth;

    public TrailerViewAdapter(TrailersItemNavigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemTrailerBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_trailer, parent, false);
        binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(viewWidth, viewHeight));
        return new TrailerViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);
        final TrailerViewModel viewModel = new TrailerViewModel(navigator);
        viewModel.setTrailer(trailer);
        holder.binding.setViewmodel(viewModel);
    }

    @Override
    public int getItemCount() {
        return this.trailers.size();
    }

    public void onDestroy() {
        navigator = null;
    }

    public void refreshAdapter(List<Trailer> trailers) {
        this.trailers.clear();
        this.trailers.addAll(trailers);
        notifyDataSetChanged();
    }

    public void setViewSize(int viewWidth, int viewHeight) {
        this.viewHeight = viewHeight;
        this.viewWidth = viewWidth;
    }
}