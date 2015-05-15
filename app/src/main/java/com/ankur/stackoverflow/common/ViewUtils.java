package com.ankur.stackoverflow.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ankur.stackoverflow.utils.Utils;

public class ViewUtils {

    private static final String LOG_TAG = "VIEW_UTILS";

    private static Toast        sToast;

    /*
     * If the data is empty, the view is hidden
     */
    public static void setupTextView(TextView tv, CharSequence chars) {
        if (TextUtils.isEmpty(chars)) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(chars);
        }
    }

    public static void setVisible(int visibility, View... views) {
        if (views != null) {
            for (View view : views) {
                view.setVisibility(visibility);
            }
        }
    }

    public static void showShortToast(final Context context, final String message) {

        if (Looper.myLooper() == Looper.getMainLooper()) {
            // It is the UI thread

            toast(context, message, Toast.LENGTH_SHORT);
        } else {
            AsyncUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast(context, message, Toast.LENGTH_SHORT);
                }
            });
        }
    }

    public static void showLongToast(final Context context, final String message) {

        if (Looper.myLooper() == Looper.getMainLooper()) {
            // It is the UI thread

            toast(context, message, Toast.LENGTH_LONG);
        } else {
            AsyncUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast(context, message, Toast.LENGTH_LONG);
                }
            });
        }
    }

    private static void toast(Context context, String msg, int length) {
        if (sToast != null)
            sToast.cancel();

//        sToast = Toast.makeText(context, Utils.getSpannableString(context, msg), length);
        sToast = Toast.makeText(context, msg, length);
        sToast.show();
    }

    public static TextView setRightDrawable(TextView textView, int drawableResId, float scaleFactor) {
        Context context = textView.getContext();
        Drawable drawable = context.getResources().getDrawable(drawableResId);
        int width = (int) (drawable.getIntrinsicWidth() * scaleFactor);
        int height = (int) (drawable.getIntrinsicHeight() * scaleFactor);
        int padding = (int) (Utils.dpToPixels(context, 4) * scaleFactor);
        drawable.setBounds(0, 0, width, height);
        textView.setCompoundDrawables(null, null, drawable, null);
        textView.setCompoundDrawablePadding(padding);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        return textView;
    }

}
