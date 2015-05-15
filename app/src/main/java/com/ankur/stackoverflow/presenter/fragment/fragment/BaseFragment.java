package com.ankur.stackoverflow.presenter.fragment.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.internal.widget.ViewUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ankur.stackoverflow.MyApplication;
import com.ankur.stackoverflow.presenter.fragment.activity.BaseActivity;
import com.ankur.stackoverflow.utils.LogUtils;

public abstract class BaseFragment extends Fragment {

    private static final String LOG_TAG = "BASE_FRAGMENT";

    protected BaseActivity mActivity;

    protected MyApplication mApplication;

    /*
     * Method to standardize tags of all fragments
     */
    public abstract String getFragmentTag();

    /*
     * Use this method instead of getActivity() if needed Otherwise, always use
     * mActivity
     */
    public BaseActivity getmActivity() {
        return mActivity;
    }

    public void onNewBundle(Bundle bundle) {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onNewBundle(): " + this.getClass().getSimpleName() + ": "
                    + getFragmentTag());
        if (getArguments() != null && bundle != null)
            getArguments().putAll(bundle);
    }

    @Override
    public void onAttach(Activity activity) {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onAttach(): " + this.getClass().getSimpleName() + ": "
                    + getFragmentTag());
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onCreate(): " + this.getClass().getSimpleName() + ": "
                    + getFragmentTag());
        super.onCreate(savedInstanceState);
        mApplication = (MyApplication) mActivity.getApplicationContext();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "onCreateOptionsMenu()");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onCreateView(): " + this.getClass().getSimpleName() + ": "
                    + getFragmentTag());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onViewCreated(): " + this.getClass().getSimpleName() + ": "
                    + getFragmentTag());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onActivityCreated(): " + this.getClass().getSimpleName() + ": "
                    + getFragmentTag());
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onStart(): " + this.getClass().getSimpleName() + ": "
                    + getFragmentTag());
        super.onStart();
    }

    @Override
    public void onResume() {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onResume(): " + this.getClass().getSimpleName() + ": "
                    + getFragmentTag());
        super.onResume();
    }

    @Override
    public void onPause() {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onPause(): " + this.getClass().getSimpleName() + ": "
                    + getFragmentTag());
        super.onPause();
    }

    @Override
    public void onStop() {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onStop(): " + this.getClass().getSimpleName() + ": "
                    + getFragmentTag());
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onDestroyView(): " + this.getClass().getSimpleName() + ": "
                    + getFragmentTag());
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onDestroy(): " + this.getClass().getSimpleName() + ": "
                    + getFragmentTag());
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "[LIFECYCLE] onDetach(): " + this.getClass().getSimpleName() + ": "
                    + getFragmentTag());
        mActivity = null;
        super.onDetach();
    }

    /*
     * Returns true if the fragment is a Screen.
     */
    protected boolean isScreen() {
        return false;
    }

    protected String getScreenTitle() {
        return "";
    }

    protected String getScreenSubTitle() {
        return null;
    }

    public boolean isOptionsMenuAllowed() {
        return false;
    }

    private void setActionBarTitle(ActionBar actionBar, CharSequence title) {
        LogUtils.debugLog(LOG_TAG, "HERE");
        if (mActivity != null) {
            // On certain devices (LG, running 4.1.2) setting spannable string
            // in title leads to ISE
            int actionBarTitleId = getResources().getIdentifier("action_bar_title", "id", "android");
            TextView titleTextView = (TextView) mActivity.findViewById(actionBarTitleId);
            if (titleTextView != null) {
                titleTextView.setText(title);
                return;
            }
        }
        actionBar.setTitle(title);
    }

    public void setupActionBar() {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "setupActionBar(): " + getFragmentTag());

        if (!isAdded()) {
            return;
        }

        String title = getScreenTitle();
        String subTitle = getScreenSubTitle();

        try {
            // FIXME Workaround for full-screen activities.
            ActionBar actionBar = mActivity.getSupportActionBar();
            if (actionBar != null) {
                if (title != null) {
                    setActionBarTitle(actionBar, title);
                }

                actionBar.setSubtitle(subTitle);

                // int icon = getActionBarIcon();

                // if (icon == -1) {
                // actionBar.setIcon(new
                // ColorDrawable(android.R.color.transparent));
                // } else {
                // Drawable iconDrawable = getResources().getDrawable(icon);
                // int drawableSizeInPx = Utils.dpToPixels(mActivity, 20);
                // iconDrawable.setBounds(0, 0, drawableSizeInPx,
                // drawableSizeInPx);
                // actionBar.setIcon(iconDrawable);
                // }

                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        } catch (Exception e) {
            LogUtils.warnLog(getFragmentTag(), "Error", e);
        }
    }

}
