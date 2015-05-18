package com.ankur.stackoverflow.common;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    private static void toast(Context context, String msg, int length) {
        if (sToast != null)
            sToast.cancel();

        sToast = Toast.makeText(context, msg, length);
        sToast.show();
    }

}
