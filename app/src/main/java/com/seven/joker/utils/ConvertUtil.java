package com.seven.joker.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

public class ConvertUtil {

    public static String convertFeedUgc(int count) {
        if (count < 10000) {
            return String.valueOf(count);
        }

        return count / 10000 + "万";
    }

}
