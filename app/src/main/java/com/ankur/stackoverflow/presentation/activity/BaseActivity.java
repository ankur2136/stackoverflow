package com.ankur.stackoverflow.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.ankur.stackoverflow.MyApplication;
import com.ankur.stackoverflow.R;
import com.ankur.stackoverflow.common.CompatUtils;
import com.ankur.stackoverflow.utils.LogUtils;
import com.ankur.stackoverflow.utils.Utils;

public abstract class BaseActivity extends ActionBarActivity {

    private static final String LOG_TAG   = "BASE_ACTIVITY";

    private MyApplication       mMyApplication;

    private boolean             mIsPaused = false;

    private Menu                mToolbarMenu;

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
    public boolean onPrepareOptionsMenu(Menu menu) {
        mToolbarMenu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    public void clearMenu() {
        mToolbarMenu.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onCreate(): " + this.getClass().getSimpleName());
        super.onCreate(savedInstanceState);
        mMyApplication = (MyApplication) getApplicationContext();

        if (savedInstanceState == null) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, this.getClass().getSimpleName() + " : onCreateOptionsMenu()");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onStart() {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onStart(): " + this.getClass().getSimpleName());
        super.onStart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onNewIntent(): " + this.getClass().getSimpleName());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onWindowFocusChanged(" + hasFocus + "): "
                    + this.getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onResume(): " + this.getClass().getSimpleName());
        mIsPaused = false;
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onPause(): " + this.getClass().getSimpleName());
        mIsPaused = true;
        super.onPause();
    }

    @Override
    public void onStop() {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onStop(): " + this.getClass().getSimpleName());
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onDestroy(): " + this.getClass().getSimpleName());
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onActivityResult(): " + this.getClass().getSimpleName());
        super.onActivityResult(arg0, arg1, arg2);
    }

    public MyApplication getMyApplication() {
        return mMyApplication;
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
            ((HomeActivity) this).onKeyUp(KeyEvent.KEYCODE_BACK, null);
            ((HomeActivity) this).onBackPressed();
        } else {
            backPress();
        }
    }

    public boolean isPaused() {
        return mIsPaused;
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
