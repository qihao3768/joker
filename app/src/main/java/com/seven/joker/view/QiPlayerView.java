package com.seven.joker.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.seven.joker.R;
import com.seven.joker.exoplayer.IPlayTarget;
import com.seven.joker.exoplayer.PageListPlay;
import com.seven.joker.exoplayer.PageListPlayManager;
import com.seven.joker.utils.DeviceUtil;

public class QiPlayerView extends FrameLayout implements IPlayTarget, PlayerControlView.VisibilityListener, Player.EventListener {
    private View bufferView;
    public QiImageView cover, blur;
    private ImageView playButton;
    private String category;
    private String videoUrl;
    private boolean isPlaying;

    public QiPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public QiPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QiPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.player_view, this, true);
        bufferView = findViewById(R.id.buffer_view);
        cover = findViewById(R.id.cover);
        blur = findViewById(R.id.blur_background);
        playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying()) {
                    inActive();
                } else {
                    onActive();
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PageListPlay pageListPlay = PageListPlayManager.get(category);
        pageListPlay.playerControlView.show();
        return true;
    }

    public void bindData(String category, int videoWidth, int videoHeight, String coverUrl, String videoUrl) {
        this.category = category;
        this.videoUrl = videoUrl;
        cover.setImageUrl(cover, coverUrl, false);
        if (videoWidth < videoHeight) {
            blur.setBlurImageUrl(coverUrl, 10);
            blur.setVisibility(VISIBLE);
        } else {
            blur.setVisibility(INVISIBLE);
        }

        setSize(videoWidth, videoHeight);
    }

    protected void setSize(int videoWidth, int videoHeight) {
        int maxWidth = DeviceUtil.deviceWidth();
        int maxHeight = maxWidth;

        int layoutWidth = maxWidth;
        int layoutHeight = 0;
        int coverWidth;
        int coverHeight;
        if (videoWidth >= videoHeight) {
            coverWidth = maxWidth;
            coverHeight = layoutHeight = (int) (videoHeight / (videoWidth * 1.0f / maxWidth));
        } else {
            coverWidth = (int) (videoWidth / (videoHeight * 1.0f / maxHeight));
            coverHeight = layoutHeight = maxHeight;
        }

        ViewGroup.LayoutParams videoParams = getLayoutParams();
        videoParams.width = layoutWidth;
        videoParams.height = layoutHeight;
        setLayoutParams(videoParams);

        ViewGroup.LayoutParams blurParams = blur.getLayoutParams();
        blurParams.width = layoutWidth;
        blurParams.height = layoutHeight;
        blur.setLayoutParams(blurParams);

        FrameLayout.LayoutParams coverParams = (LayoutParams) cover.getLayoutParams();
        coverParams.width = coverWidth;
        coverParams.height = coverHeight;
        coverParams.gravity = Gravity.CENTER;
        cover.setLayoutParams(coverParams);

        FrameLayout.LayoutParams playButtonParams = (LayoutParams) playButton.getLayoutParams();
        playButtonParams.gravity = Gravity.CENTER;
        playButton.setLayoutParams(playButtonParams);

    }

    @Override
    public ViewGroup getOwner() {
        return this;
    }

    @Override
    public void onActive() {
        PageListPlay pageListPlay = PageListPlayManager.get(category);
        PlayerView playerView = pageListPlay.playerView;
        PlayerControlView controlView = pageListPlay.playerControlView;
        SimpleExoPlayer exoPlayer = pageListPlay.exoPlayer;
        ViewParent parent = playerView.getParent();
        if (parent != this) {
            if (parent != null) {
                ((ViewGroup) parent).removeView(playerView);
            }
            ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
            this.addView(playerView, 1, layoutParams);
        }

        ViewParent controlParent = controlView.getParent();
        if (controlParent != this) {
            if (controlParent != null) {
                ((ViewGroup) controlParent).removeView(controlView);
            }
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.BOTTOM;
            this.addView(controlView, params);
            controlView.setVisibilityListener(this);
        }
        controlView.show();
        if (TextUtils.equals(pageListPlay.playUrl, videoUrl)) {
            onPlayerStateChanged(true, Player.STATE_READY);
        } else {
            MediaSource mediaSource = PageListPlayManager.creatMediaSource(videoUrl);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
            exoPlayer.addListener(this);
        }
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void inActive() {
        PageListPlay pageListPlay = PageListPlayManager.get(category);
        pageListPlay.exoPlayer.setPlayWhenReady(false);
        playButton.setVisibility(VISIBLE);
        playButton.setImageResource(R.drawable.icon_video_play);
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isPlaying = false;
        bufferView.setVisibility(GONE);
        cover.setVisibility(VISIBLE);
        playButton.setVisibility(VISIBLE);
        playButton.setImageResource(R.drawable.icon_video_play);
    }

    @Override
    public void onVisibilityChange(int visibility) {
        playButton.setVisibility(visibility);
        playButton.setImageResource(isPlaying() ? R.drawable.icon_video_pause : R.drawable.icon_video_play);

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        PageListPlay pageListPlay = PageListPlayManager.get(category);
        SimpleExoPlayer exoPlayer = pageListPlay.exoPlayer;
        if (playbackState == Player.STATE_READY && exoPlayer.getBufferedPosition() != 0) {
            cover.setVisibility(INVISIBLE);
            bufferView.setVisibility(INVISIBLE);
        } else if (playbackState == Player.STATE_BUFFERING) {
            bufferView.setVisibility(VISIBLE);
        }

        isPlaying = playbackState == Player.STATE_READY && exoPlayer.getBufferedPosition() != 0 && playWhenReady;
        playButton.setImageResource(isPlaying() ? R.drawable.icon_video_pause : R.drawable.icon_video_play);
    }
}
