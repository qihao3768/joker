package com.seven.joker.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.seven.joker.utils.DeviceUtil;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class QiImageView extends AppCompatImageView {
    public QiImageView(Context context) {
        super(context);
    }

    public QiImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QiImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @BindingAdapter(value = {"image_url", "isCircle"}, requireAll = true)
    public static void setImageUrl(QiImageView view, String imageUrl, boolean isCircle) {
        RequestBuilder<Drawable> builder = Glide.with(view).load(imageUrl);
        if (isCircle) {
            builder.transform(new CircleCrop());
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null && layoutParams.width > 0 && layoutParams.height > 0) {
            builder.override(layoutParams.width, layoutParams.height);
        }
        builder.into(view);
    }

    public void bindData(int imageWidth, int imageHeight, int marginLeft, String imageUrl) {
        bindData(imageWidth, imageHeight, marginLeft, DeviceUtil.deviceWidth(), DeviceUtil.deviceHeight(), imageUrl);
    }

    public void bindData(int imageWidth, int imageHeight, int marginLeft, int maxWidth, int maxHeight, String imageUrl) {
        if (imageHeight <= 0 || imageWidth <= 0) {
            Glide.with(this).load(imageUrl).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    int height = resource.getIntrinsicHeight();
                    int width = resource.getIntrinsicWidth();
                    setSize(width, height, marginLeft, maxWidth, maxHeight);
                    setImageDrawable(resource);
                }
            });
            return;
        }
        setSize(imageWidth, imageHeight, marginLeft, maxWidth, maxHeight);
        setImageUrl(this, imageUrl, false);
    }

    private void setSize(int width, int height, int marginLeft, int maxWidth, int maxHeight) {
        int finalWidth, finalHeight;
        if (width > height) {
            finalWidth = maxWidth;
            finalHeight = (int) (height / (width * 1.0f / finalWidth));
        } else {
            finalHeight = maxHeight;
            finalWidth = (int) (width / (height * 1.0f / finalHeight));
        }
        ViewGroup.LayoutParams marginParams = getLayoutParams();
        marginParams.width = finalWidth;
        marginParams.height = finalHeight;
        if (marginParams instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) marginParams).leftMargin = height > width ? DeviceUtil.dp2px(marginLeft) : 0;
        } else if (marginParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) marginParams).leftMargin = height > width ? DeviceUtil.dp2px(marginLeft) : 0;
        }
        setLayoutParams(marginParams);
    }

    public void setBlurImageUrl(String coverUrl, int radius) {
        Glide.with(this).load(coverUrl).override(50).transform(new BlurTransformation()).dontAnimate().into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                setBackground(resource);
            }
        });

    }
}
