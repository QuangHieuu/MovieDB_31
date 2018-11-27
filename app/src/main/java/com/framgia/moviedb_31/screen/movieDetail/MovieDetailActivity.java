package com.framgia.moviedb_31.screen.movieDetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.framgia.moviedb_31.R;
import com.framgia.moviedb_31.databinding.ActivityMovieDetailBinding;
import com.framgia.moviedb_31.screen.Youtube;

public class MovieDetailActivity extends AppCompatActivity {
    private ActivityMovieDetailBinding mBinding;

    private static final String EXTRA_KEY = "EXTRA_KEY";

    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_KEY, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        MovieDetailViewModel viewModel =
                new MovieDetailViewModel(getIntent().getStringExtra(EXTRA_KEY));
        mBinding.setViewModel(viewModel);
        mBinding.textViewTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Youtube.getIntentYoutube(MovieDetailActivity.this,
                        mBinding.getViewModel().getKeyYoutube()));
            }
        });
    }

    @Override
    protected void onStop() {
        mBinding.getViewModel().onStop();
        super.onStop();
    }
}
