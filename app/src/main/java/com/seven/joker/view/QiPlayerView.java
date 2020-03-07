package com.seven.joker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.seven.joker.R;
import com.seven.joker.utils.DeviceUtil;

public class QiPlayerView extends FrameLayout {
    private View bufferView;
    public QiImageView cover, blur;
    private ImageView playButton;
    private String category;
    private String videoUrl;

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

}
