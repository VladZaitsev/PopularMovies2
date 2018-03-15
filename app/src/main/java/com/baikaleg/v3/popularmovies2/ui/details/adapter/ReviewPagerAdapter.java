package com.baikaleg.v3.popularmovies2.ui.details.adapter;

import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baikaleg.v3.popularmovies2.R;
import com.baikaleg.v3.popularmovies2.data.model.Review;
import com.baikaleg.v3.popularmovies2.databinding.ItemReviewsBinding;

import java.util.ArrayList;
import java.util.List;

public class ReviewPagerAdapter extends PagerAdapter {
    private static final String TAG = ReviewPagerAdapter.class.getSimpleName();

    private List<Review> reviews = new ArrayList<>();

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        ItemReviewsBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_reviews, container, false);

        final ReviewItemViewModel viewModel = new ReviewItemViewModel();
        viewModel.setReview(reviews.get(position));
        binding.setReviewmodel(viewModel);

        container.addView(binding.getRoot());
        return binding.getRoot();
    }


    public void refreshAdapter(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        try {
            container.removeView((View) object);
        } catch (Exception e) {
            Log.i(TAG, "failed to destroy reviewItem");
        }
    }
}