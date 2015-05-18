package com.ankur.stackoverflow.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ankur.stackoverflow.R;
import com.ankur.stackoverflow.presentation.fragment.BaseFragment;
import com.ankur.stackoverflow.utils.LogUtils;

public class NavigationUtils {

    private static final String LOG_TAG          = "NAVIGATION_UTILS";

    public static final int     NO_ANIMATION     = 0;

    public static final int     SLIDE_FROM_RIGHT = 1;

    public static final int     SLIDE_FROM_TOP   = 2;

    /*
     * Returns reference to fragment which is in container after the transaction
     * process (either previous instance or a new one). Returns null if the
     * transaction fails.
     */
    public static Fragment startFragment(FragmentManager fragmentManager, int container, Fragment fragment,
            boolean addToBackStack, int customAnimation) {

        String tag = "TAG_NOT_FOUND";
        if (fragment instanceof BaseFragment) {
            tag = ((BaseFragment) fragment).getFragmentTag();
        }

        // Check the topmost visible fragment
        // PS: Can't rely on back stack because not all fragments are added to
        // back stack
        BaseFragment frag = (BaseFragment) fragmentManager.findFragmentByTag(tag);
        if (frag != null && frag.isVisible()) {
            LogUtils.infoLog(LOG_TAG, "A fragment with same tag is already visible. Tag: " + tag);
            frag.onNewBundle(fragment.getArguments());
            fragment = null;
            return frag;
        }

        /*
         * TODO: uncomment for popping back to primary fragment
         * 
         * if (fragment instanceof HomeFragment && addToBackStack) {
         * LogUtils.debugLog(LOG_TAG, "Trying to pop back to HomeFragment");
         * fragmentManager.popBackStackImmediate(null,
         * FragmentManager.POP_BACK_STACK_INCLUSIVE); fragment =
         * fragmentManager.findFragmentById(container); return fragment; }
         */

        LogUtils.infoLog(LOG_TAG, "Trying to replace with new instance: " + tag);

        try {
            performFragmentTransaction(fragmentManager, container, fragment, tag, addToBackStack, false,
                    customAnimation);
            return fragment;
        } catch (Exception e) {
            LogUtils.errorLog(LOG_TAG, "Can't perform commit()", e);
            try {
                performFragmentTransaction(fragmentManager, container, fragment, tag, addToBackStack, true,
                        customAnimation);
                return fragment;
            } catch (Exception ex) {
                LogUtils.errorLog(LOG_TAG, "Can't perform commitAllowingStateLoss()", ex);
            }
        }

        return null;

    }

    private static void performFragmentTransaction(FragmentManager fragmentManager, int container, Fragment fragment,
            String tag, boolean addToBackStack, boolean allowStateLoss, int customAnimation) throws Exception {

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (addToBackStack) {
            switch (customAnimation) {
            case SLIDE_FROM_RIGHT:
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,
                        R.anim.exit_to_right);
                break;
            case SLIDE_FROM_TOP:
                transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.pop_out, R.anim.pop_in);
                break;
            case NO_ANIMATION:
                break;
            default:
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,
                        R.anim.exit_to_right);
            }
        }

        // FragmentManager.findFragmentByTag(String) relies on the tag supplied
        // here
        transaction.replace(container, fragment, tag);

        if (transaction.isAddToBackStackAllowed()) {
            if (addToBackStack)
                transaction.addToBackStack(null);
        } else {
            LogUtils.infoLog(LOG_TAG, "addToBackStack() not allowed");
        }

        if (allowStateLoss) {
            LogUtils.infoLog(LOG_TAG, "Commiting transaction allowing state loss");

            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }
    }

}