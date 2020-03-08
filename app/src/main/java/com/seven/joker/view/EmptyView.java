package com.seven.joker.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.seven.joker.R;

public class EmptyView extends LinearLayout {
    private ImageView image;
    private TextView text;
    private Button button;

    public EmptyView(@NonNull Context context) {
        this(context, null);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.empty_view, this, true);
        image = findViewById(R.id.empty_image);
        text = findViewById(R.id.empty_text);
        button = findViewById(R.id.empty_button);
    }

    public void setImage(@DrawableRes int iamgeRes) {
        image.setImageResource(iamgeRes);
    }

    public void setText(String textRes) {
        if (!TextUtils.isEmpty(textRes)) {
            text.setText(textRes);
        }
    }

    public void setButton(String text, View.OnClickListener listener) {
        if (!TextUtils.isEmpty(text)) {
            button.setText(text);
            button.setVisibility(VISIBLE);
            button.setOnClickListener(listener);
        }

    }
}
