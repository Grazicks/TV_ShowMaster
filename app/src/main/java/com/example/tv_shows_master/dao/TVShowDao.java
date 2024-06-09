package com.example.tv_shows_master.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tv_shows_master.models.TVShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;


@Dao
public interface TVShowDao {

    @Query("SELECT * FROM tvShows")
    Flowable<List<TVShow>> getWatchlist();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addToWatchlist(TVShow tvShow);

    @Delete
    Completable removeFromWatchlist(TVShow tvShow);

}
