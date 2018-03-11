package com.baikaleg.v3.popularmovies2.ui.details;

import android.content.Context;
import android.databinding.Bindable;

import com.baikaleg.v3.popularmovies2.MovieViewModel;
import com.baikaleg.v3.popularmovies2.data.Repository;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class DetailsViewModel extends MovieViewModel {

    private int height, width;

    @Inject
    public DetailsViewModel(Repository repository, Context context) {
        super(repository, context);
    }

    public void favoriteClicked() {
        setFavorite(!getFavorite());
    }

    @Bindable
    public int getHeight() {
        return height;
    }

    @Bindable
    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
