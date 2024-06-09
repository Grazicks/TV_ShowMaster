package com.example.tv_shows_master.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.tv_shows_master.database.TVShowsDatabase;
import com.example.tv_shows_master.models.TVShow;

import java.util.List;

import io.reactivex.Flowable;

public class WatchlistViewModel extends AndroidViewModel {
    private final TVShowsDatabase tvShowsDatabase;

    public WatchlistViewModel(@NonNull Application application) {
        super(application);
        tvShowsDatabase=TVShowsDatabase.getTvShowsDatabase(application);
    }
    public Flowable<List<TVShow>> loadWatchlist(){
        return  tvShowsDatabase.tvShowDAO().getWatchlist();
    }
}
