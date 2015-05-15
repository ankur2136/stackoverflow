package com.ankur.stackoverflow.common;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.internal.widget.ViewUtils;

import com.ankur.stackoverflow.R;
import com.ankur.stackoverflow.presenter.fragment.activity.BaseActivity;
import com.ankur.stackoverflow.presenter.fragment.fragment.BaseFragment;
import com.ankur.stackoverflow.utils.LogUtils;

/**
 * Created by arpit on 24/03/15.
 */
public class NavigationUtils {

    private static final String LOG_TAG          = "NAVIGATION_UTILS";

    public static final int     NO_ANIMATION     = 0;

    public static final int     SLIDE_FROM_RIGHT = 1;

    public static final int     SLIDE_FROM_TOP   = 2;

    public static boolean       DO_NOT_CLEAR_TOP = false;

    public static void addFragment(FragmentManager fragmentManager, Fragment fragment, int containerView,
            boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (transaction.isAddToBackStackAllowed()) {
            if (addToBackStack)
                transaction.addToBackStack(null);
        } else {
            LogUtils.infoLog(LOG_TAG, "addToBackStack() not allowed");
        }

        transaction.add(containerView, fragment).commit();
    }

    public static void showFragment(FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager.beginTransaction().setCustomAnimations(NO_ANIMATION, NO_ANIMATION).show(fragment).commit();
    }

    public static void hideFragment(FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager.beginTransaction().setCustomAnimations(NO_ANIMATION, NO_ANIMATION).hide(fragment).commit();
    }

    public static boolean removeFragment(FragmentManager fragmentManager, String fragmentTag) {
        BaseFragment frag = (BaseFragment) fragmentManager.findFragmentByTag(fragmentTag);
        if (frag != null) {
            LogUtils.infoLog(LOG_TAG, "Fragment with tag " + fragmentTag + " found!");
            fragmentManager.beginTransaction().remove(frag).commit();
            return true;
        }
        return false;
    }

    public static boolean removeFragment(FragmentManager fragmentManager, Fragment frag) {
        if (frag != null) {
            fragmentManager.popBackStackImmediate();
            fragmentManager.beginTransaction().remove(frag).commit();
            return true;
        }
        return false;
    }

    /*
     * Returns reference to fragment which is in container after the transaction
     * process (either previous instance or a new one). Returns null if the
     * transaction fails.
     */
    public static Fragment startFragment(FragmentManager fragmentManager, int container, Fragment fragment,
            boolean addToBackStack, int customAnimation) {

        String tag = "TAG_NOT_FOUND"; // FIXME This should never happen

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

        /*
         * NOTE: Code for reusing old instance
         * 
         * LogUtils.infoLog(LOG_TAG, "Reusing old instance: " + tag); int
         * backStackCount = fragmentManager.getBackStackEntryCount(); int oldPos
         * = 0;
         * 
         * for (int i = backStackCount - 1; i >= 0; i--) {
         * FragmentManager.BackStackEntry entry =
         * fragmentManager.getBackStackEntryAt(i); if
         * (entry.getName().equals(tag)) { oldPos = i; break; } }
         * LogUtils.infoLog(LOG_TAG, "Old fragment found at: " + oldPos);
         * 
         * for (int i = backStackCount - 1; i >= oldPos; i--) {
         * LogUtils.infoLog(LOG_TAG, "Popping fragment at: " + i);
         * fragmentManager.popBackStackImmediate(); }
         * 
         * FragmentTransaction transaction = fragmentManager.beginTransaction();
         * if (backStack)
         * transaction.setCustomAnimations(android.R.anim.fade_in,
         * android.R.anim.fade_out); transaction.replace(container, fragment,
         * tag); if (backStack) transaction.addToBackStack(tag);
         * transaction.commit();
         */
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

    private static void performDialogFragmentTransaction(DialogFragment dialogFragment,
            FragmentManager fragmentManager, String tag, boolean allowStateLoss) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(dialogFragment, tag);
        if (allowStateLoss) {
            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }
    }

    public static Fragment startFragmentInMainPanel(BaseActivity activity, Fragment fragment, boolean addToBackStack,
            int customAnimation) {
        // if (activity instanceof BaseHomeActivity) {
        // ((BaseHomeActivity) activity).collapsePlayer();
        // }

        return startFragment(activity.getSupportFragmentManager(), R.id.fl_fragment_container, fragment,
                addToBackStack, customAnimation);
    }

    public static void startActivity(Context context, Intent intent, boolean collapsePanel, boolean catchExceptions) {
        // if (collapsePanel) {
        // if (context instanceof BaseHomeActivity) {
        // ((BaseHomeActivity) context).collapsePlayer();
        // }
        // }

        if (DO_NOT_CLEAR_TOP) {
            return;
        }
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        if (catchExceptions) {
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                LogUtils.errorLog(LOG_TAG, "Activity not found", e);
            }
        } else {
            context.startActivity(intent);
        }
    }

    public static void startActivity(Context context, Intent intent, boolean collapsePanel) {
        startActivity(context, intent, collapsePanel, true);
    }

    public static void startActivity(Context context, Intent intent) {
        startActivity(context, intent, true);
    }

    public static void startStoreListingScreen(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(context, goToMarket, false, false);
        } catch (ActivityNotFoundException e) {
            startActivity(
                    context,
                    new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="
                            + packageName)));
        }
    }

    public static void startAppInfoScreen(Context context, String packageName) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(context, intent, false);
    }

    public static void startDialogFragment(DialogFragment dialogFragment, FragmentManager fragmentManager, String tag) {
        try {
            performDialogFragmentTransaction(dialogFragment, fragmentManager, tag, false);
        } catch (Exception e) {
            try {
                performDialogFragmentTransaction(dialogFragment, fragmentManager, tag, true);
            } catch (Exception ex) {

            }
        }
    }

}