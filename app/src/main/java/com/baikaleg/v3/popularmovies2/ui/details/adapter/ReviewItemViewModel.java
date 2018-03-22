package com.baikaleg.v3.popularmovies2.ui.details.adapter;

import android.databinding.BaseObservable;
import android.databinding.Observable;
import android.databinding.ObservableField;

import com.baikaleg.v3.popularmovies2.data.model.Review;

public class ReviewItemViewModel extends BaseObservable {

    public final ObservableField<String> content = new ObservableField<>();
    public final ObservableField<String> author = new ObservableField<>();

    private final ObservableField<Review> reviewObservable = new ObservableField<>();

    ReviewItemViewModel() {
        reviewObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Review review = reviewObservable.get();
                if (review != null) {
                    content.set(review.getContent());
                    author.set(review.getAuthor());
                    notifyChange();
                }
            }
        });
    }

    void setReview(Review review) {
        reviewObservable.set(review);
    }
}