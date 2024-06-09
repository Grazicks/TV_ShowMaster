package com.example.tv_shows_master.listeners;
import com.example.tv_shows_master.models.TVShow;

public interface WatchlistListener {
    void onTVShowClicked(TVShow tvShow);
    void removeTvShowFromWatchlist(TVShow tvShow,int position);
}
