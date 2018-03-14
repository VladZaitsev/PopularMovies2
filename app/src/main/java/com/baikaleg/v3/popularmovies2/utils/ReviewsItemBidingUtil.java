package com.baikaleg.v3.popularmovies2.utils;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.baikaleg.v3.popularmovies2.data.model.Review;
import com.baikaleg.v3.popularmovies2.ui.details.adapter.ReviewPagerAdapter;

import java.util.List;

public class ReviewsItemBidingUtil {

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:reviews")
    public static void setItems(ViewPager pager, List<Review> items) {
        ReviewPagerAdapter adapter = (ReviewPagerAdapter) pager.getAdapter();
        if (adapter != null)
        {
            adapter.refreshAdapter(items);
        }
    }

    @BindingAdapter("app:layout_height")
    public static void setLayoutHeight(ViewPager view, float height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) height;
        view.setLayoutParams(layoutParams);
    }
}