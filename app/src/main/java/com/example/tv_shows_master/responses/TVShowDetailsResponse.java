package com.example.tv_shows_master.responses;

import com.example.tv_shows_master.models.TVShowDetails;
import com.google.gson.annotations.SerializedName;

public class TVShowDetailsResponse {
    @SerializedName("tvShow")
    private TVShowDetails tvShowDetails;
    public TVShowDetails getTvShowDetails() {
        return tvShowDetails;
    }
}
