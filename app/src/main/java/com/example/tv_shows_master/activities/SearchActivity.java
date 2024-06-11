package com.example.tv_shows_master.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tv_shows_master.R;
import com.example.tv_shows_master.adapters.TVShowAdapter;
import com.example.tv_shows_master.databinding.ActivitySearchBinding;
import com.example.tv_shows_master.listeners.TVShowsListener;
import com.example.tv_shows_master.models.TVShow;
import com.example.tv_shows_master.viewmodels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements TVShowsListener {
    private ActivitySearchBinding activitySearchBinding;

    private SearchViewModel viewModel;
    private final List<TVShow> tvShows = new ArrayList<>();
    private TVShowAdapter tvShowAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        doInitialization();
    }
    private void doInitialization() {
        activitySearchBinding.imgBack.setOnClickListener(v -> onBackPressed());
        activitySearchBinding.rvTvShows.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        tvShowAdapter = new TVShowAdapter(tvShows, this);
        activitySearchBinding.rvTvShows.setAdapter(tvShowAdapter);
        activitySearchBinding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                currentPage = 1;
                                totalAvailablePages = 1;
                                searchTVShow(s.toString());
                            });
                        }
                    }, 500);
                } else {
                    tvShows.clear();
                    tvShowAdapter.notifyDataSetChanged();
                }
            }
        });
        activitySearchBinding.rvTvShows.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activitySearchBinding.rvTvShows.canScrollVertically(1)) {
                    if (!activitySearchBinding.inputSearch.getText().toString().isEmpty()) {
                        if (currentPage < totalAvailablePages) {
                            currentPage += 1;
                            searchTVShow(activitySearchBinding.inputSearch.getText().toString());
                        }
                    }
                }
            }
        });
        activitySearchBinding.inputSearch.requestFocus();
    }
    private void searchTVShow(String query) {
        toggleLoading();
        viewModel.searchTVShow(query, currentPage).observe(this, tvShowsResponse -> {
            toggleLoading();
            if (tvShowsResponse != null) {
                totalAvailablePages = tvShowsResponse.getTotalPages();
                if (tvShowsResponse.getTvShows() != null) {
                    int oldCount = tvShows.size();
                    tvShows.addAll(tvShowsResponse.getTvShows());
                    tvShowAdapter.notifyItemRangeInserted(oldCount, tvShows.size());
                }
            }
        });
    }
    private void toggleLoading() {
        if (currentPage == 1) {
            activitySearchBinding.setIsLoading(activitySearchBinding.getIsLoading() == null || !activitySearchBinding.getIsLoading());
        } else {
            activitySearchBinding.setIsLoadingMore(activitySearchBinding.getIsLoadingMore() == null || !activitySearchBinding.getIsLoadingMore());
        }
    }
    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);
    }
}