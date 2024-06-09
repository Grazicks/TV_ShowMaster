package com.example.tv_shows_master.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tv_shows_master.repositories.TVShowDetailsRepository;
import com.example.tv_shows_master.responses.TVShowDetailsResponse;

public class TVShowDetailsViewModel extends ViewModel {
    private final TVShowDetailsRepository tvShowDetailsRepository;


    public TVShowDetailsViewModel() {
        tvShowDetailsRepository = new TVShowDetailsRepository();
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId) {
        return tvShowDetailsRepository.getTVShowDetails(tvShowId);
    }

}
