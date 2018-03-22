package com.baikaleg.v3.popularmovies2.ui.details.trailers;

import android.databinding.BaseObservable;
import android.databinding.Observable;
import android.databinding.ObservableField;

import com.baikaleg.v3.popularmovies2.data.model.Trailer;

public class TrailerViewModel extends BaseObservable {

    private final TrailersItemNavigator navigator;

    public final ObservableField<String> key = new ObservableField<>();

    public final ObservableField<String> site = new ObservableField<>();

    public final ObservableField<String> type = new ObservableField<>();

    public final ObservableField<Trailer> trailerObservable = new ObservableField<>();

    public TrailerViewModel( TrailersItemNavigator navigator) {
        this.navigator = navigator;
        trailerObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Trailer trailer = trailerObservable.get();
                if (trailer != null) {
                    key.set(trailer.getKey());
                    site.set(trailer.getSite());
                    type.set(trailer.getType());
                    notifyChange();
                }
            }
        });
    }

    public void onTrailerClicked() {
        navigator.onTrailerClicked(trailerObservable.get());
    }

    public void setTrailer(Trailer trailer) {
        trailerObservable.set(trailer);
    }
} 