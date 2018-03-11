package com.baikaleg.v3.popularmovies2.data.source;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.baikaleg.v3.popularmovies2.data.source.MovieContract.MovieEntry;

public class MoviesDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies_db";

    private static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";

    private static final String COMMA = ", ";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry.ID + INTEGER_TYPE + " PRIMARY KEY," +
                    MovieEntry.TITLE + TEXT_TYPE + COMMA +
                    MovieEntry.POSTER_PATH + TEXT_TYPE +
                    " )";


    MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}