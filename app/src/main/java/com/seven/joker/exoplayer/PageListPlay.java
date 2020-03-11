package com.seven.joker.exoplayer;

import android.net.Uri;
import android.view.LayoutInflater;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSinkFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.seven.joker.QiApplication;
import com.seven.joker.R;

public class PageListPlay {
    public SimpleExoPlayer exoPlayer;
    public PlayerView playerView;
    public PlayerControlView playerControlView;
    public String playUrl;


    public PageListPlay() {
        QiApplication qiApplication = QiApplication.getInstance();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(qiApplication, new DefaultRenderersFactory(qiApplication), new DefaultTrackSelector(), new DefaultLoadControl());

        playerView = (PlayerView) LayoutInflater.from(qiApplication).inflate(R.layout.exo_player_view, null, false);

        playerControlView = (PlayerControlView) LayoutInflater.from(qiApplication).inflate(R.layout.layout_exo_player_contorller_view, null, false);
        playerView.setPlayer(exoPlayer);
        playerControlView.setPlayer(exoPlayer);
    }

    public void release() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop(true);
            exoPlayer.release();
            exoPlayer = null;
        }

        if (playerView != null) {
            playerView.setPlayer(null);
            playerView = null;
        }
        if (playerControlView != null) {
            playerControlView.setPlayer(null);
            playerControlView.setVisibilityListener(null);
            playerControlView = null;
        }

    }
}
