package com.example.tv_shows_master.network;

import com.example.tv_shows_master.responses.TVShowDetailsResponse;
import com.example.tv_shows_master.responses.TVShowsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("most-popular")
    Call<TVShowsResponse> getMostPopularTVShows(@Query("page") int page);
    @GET("show-details")
    Call<TVShowDetailsResponse> getShowDetails(@Query("q") String tvShowId);
}
