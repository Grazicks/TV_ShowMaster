package com.example.tv_shows_master.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tv_shows_master.database.TVShowsDatabase;
import com.example.tv_shows_master.models.TVShow;
import com.example.tv_shows_master.repositories.TVShowDetailsRepository;
import com.example.tv_shows_master.responses.TVShowDetailsResponse;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

public class TVShowDetailsViewModel extends AndroidViewModel {
    private final TVShowDetailsRepository tvShowDetailsRepository;
    private final TVShowsDatabase tvShowsDatabase;

    public TVShowDetailsViewModel(@NonNull Application application) {
        super(application);
        tvShowDetailsRepository = new TVShowDetailsRepository();
        tvShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application);
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId) {
        return tvShowDetailsRepository.getTVShowDetails(tvShowId);
    }
    public Completable addToWatchlist(TVShow tvShow) {
        return tvShowsDatabase.tvShowDAO().addToWatchlist(tvShow);
    }
    public Flowable<TVShow> getTvShowFromWatchlist(String tvShowId){
        return tvShowsDatabase.tvShowDAO().getTVShowFromWatchlist(tvShowId);
    }
    public Completable removeTVShowFromWatchlist(TVShow tvShow){
        return tvShowsDatabase.tvShowDAO().removeFromWatchlist(tvShow);
    }

}
