package com.framgia.moviedb_31.screen;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;
import com.framgia.moviedb_31.BuildConfig;
import com.framgia.moviedb_31.R;
import com.framgia.moviedb_31.databinding.YoutubePlayerBinding;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

public class Youtube extends YouTubeBaseActivity {

    public static Intent getIntentYoutube(Context context, String id) {
        Intent intent = new Intent(context, Youtube.class);
        intent.putExtra("KEY", id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        YoutubePlayerBinding binding =
                DataBindingUtil.setContentView(this, R.layout.youtube_player);
        binding.youtubePlayer.initialize(BuildConfig.YOUTUBE_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                            YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.setFullscreen(true);
                        youTubePlayer.setShowFullscreenButton(false);
                        youTubePlayer.loadVideo(getIntent().getStringExtra("KEY"));
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                            YouTubeInitializationResult youTubeInitializationResult) {
                        Toast.makeText(Youtube.this, youTubeInitializationResult.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
