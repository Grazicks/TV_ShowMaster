package com.example.tv_shows_master.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tv_shows_master.R;
import com.example.tv_shows_master.adapters.ImageSliderAdapter;
import com.example.tv_shows_master.databinding.ActivityTvshowDetailsBinding;
import com.example.tv_shows_master.viewmodels.TVShowDetailsViewModel;

import java.util.Locale;

public class TVShowDetailsActivity extends AppCompatActivity {
    private ActivityTvshowDetailsBinding activityTvshowDetailsBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTvshowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details);
        doInitialization();

    }
    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        activityTvshowDetailsBinding.imageBack.setOnClickListener(v -> onBackPressed());
        getTVShowDetails();
    }
    private void getTVShowDetails(){
        activityTvshowDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(getIntent().getIntExtra("id", -1));
        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(
                this, tvShowDetailsResponse -> {
                    activityTvshowDetailsBinding.setIsLoading(false);
                    if (tvShowDetailsResponse.getTvShowDetails() != null) {

                        if (tvShowDetailsResponse.getTvShowDetails().getPictures() != null) {
                            loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                        }
                        activityTvshowDetailsBinding.setTvShowImageURL(
                                tvShowDetailsResponse.getTvShowDetails().getImagePath()
                        );
                        activityTvshowDetailsBinding.imageTVShow.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.setDescription(
                                String.valueOf(
                                        HtmlCompat.fromHtml(
                                                tvShowDetailsResponse.getTvShowDetails().getDescription(),
                                                HtmlCompat.FROM_HTML_MODE_LEGACY
                                        )
                                )
                        );
                        activityTvshowDetailsBinding.txtDescription.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.txtReadMore.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.txtReadMore.setOnClickListener(v -> {
                            if (activityTvshowDetailsBinding.txtReadMore.getText().toString().equals("Read More")) {
                                activityTvshowDetailsBinding.txtDescription.setMaxLines(Integer.MAX_VALUE);
                                activityTvshowDetailsBinding.txtDescription.setEllipsize(null);
                                activityTvshowDetailsBinding.txtReadMore.setText(R.string.read_less);
                            } else {
                                activityTvshowDetailsBinding.txtDescription.setMaxLines(4);
                                activityTvshowDetailsBinding.txtDescription.setEllipsize(TextUtils.TruncateAt.END);
                                activityTvshowDetailsBinding.txtReadMore.setText(R.string.read_more);
                            }
                        });
                        activityTvshowDetailsBinding.setRating(
                                String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating())
                                )
                        );
                        if (tvShowDetailsResponse.getTvShowDetails().getGenres() != null) {
                            activityTvshowDetailsBinding.setGenre(tvShowDetailsResponse.getTvShowDetails().getGenres()[0]);
                        } else {
                            activityTvshowDetailsBinding.setGenre("N/A");

                        }
                        activityTvshowDetailsBinding.setRuntime(tvShowDetailsResponse.getTvShowDetails().getRuntime() + " Min");
                        activityTvshowDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);

                        activityTvshowDetailsBinding.btnWebSite.setOnClickListener(v -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                            startActivity(intent);
                        });

                        activityTvshowDetailsBinding.btnWebSite.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.btnEpisodes.setVisibility(View.VISIBLE);
                        loadBasicTVShowDetails();
                    }
                });

    }
    private void loadImageSlider(String[] sliderImages) {
        activityTvshowDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTvshowDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        activityTvshowDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImages.length);
        activityTvshowDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }
    private void setupSliderIndicators(int count) {

        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.background_slider_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            activityTvshowDetailsBinding.layoutSliderIndicator.addView(indicators[i]);

        }
        activityTvshowDetailsBinding.layoutSliderIndicator.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position) {
        int childCount = activityTvshowDetailsBinding.layoutSliderIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) activityTvshowDetailsBinding.layoutSliderIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive)
                );
            }
        }
    }

    private void loadBasicTVShowDetails() {
        activityTvshowDetailsBinding.textName.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.txtNetworkCountry.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.txtStatus.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.txtStarted.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.setTvShowName(getIntent().getStringExtra("name"));
        activityTvshowDetailsBinding.setNetworkCountry(
                getIntent().getStringExtra("network") + " (" +
                        getIntent().getStringExtra("country") + ")"
        );
        activityTvshowDetailsBinding.setStatus(getIntent().getStringExtra("status"));
        activityTvshowDetailsBinding.setStartDate(getIntent().getStringExtra("startDate"));

    }
}