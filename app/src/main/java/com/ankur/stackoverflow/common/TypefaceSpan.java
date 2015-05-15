package com.ankur.stackoverflow.common;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import com.ankur.stackoverflow.utils.FontUtils;

/**
 * Style a {@link android.text.Spannable} with a custom {@link android.graphics.Typeface}.
 *
 * @author Tristan Waddington
 */
public class TypefaceSpan extends MetricAffectingSpan {
    private Typeface mTypeface;

    /**
     * Load the {@link android.graphics.Typeface} and apply to a {@link android.text.Spannable}.
     */
    public TypefaceSpan(Context context, FontUtils.FontStyle fontStyle) {
        mTypeface = FontUtils.getFont(context, fontStyle);
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        p.setTypeface(mTypeface);

        // Note: This flag is required for proper typeface rendering
        p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setTypeface(mTypeface);

        // Note: This flag is required for proper typeface rendering
        tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
}
