package com.ankur.stackoverflow.presentation.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.ankur.stackoverflow.R;
import com.ankur.stackoverflow.common.CompatUtils;
import com.ankur.stackoverflow.utils.LogUtils;
import com.ankur.stackoverflow.utils.Utils;

public abstract class BaseActivity extends ActionBarActivity {

    private static final String LOG_TAG   = "BASE_ACTIVITY";

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_action_bar);
        CompatUtils.setElevation(toolbar, Utils.dpToPixels(this, 4));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(null);
    }

    @Override
    public void onStart() {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onStart(): " + this.getClass().getSimpleName());
        super.onStart();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onActivityResult(): " + this.getClass().getSimpleName());
        super.onActivityResult(arg0, arg1, arg2);
    }

    protected void backPress() {
        try {
            super.onBackPressed();
        } catch (IllegalStateException ex) {
            LogUtils.errorLog(LOG_TAG, "Cannot transact after activity onPause", ex);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // Take care of calling this method on earlier versions of
            // the platform where it doesn't exist.
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        // This will be called either automatically for you on 2.0
        // or later, or by the code above on earlier versions of the
        // platform.
        if (this instanceof HomeActivity) {
            this.onKeyUp(KeyEvent.KEYCODE_BACK, null);
            this.onBackPressed();
        } else {
            backPress();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            onBackPressed();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

}
