package com.ankur.stackoverflow.presenter.fragment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ankur.stackoverflow.presenter.fragment.presenter.BasePresenter;
import com.ankur.stackoverflow.presenter.fragment.view.BaseView;


public abstract class PresenterFragment<P extends BasePresenter> extends BaseFragment implements BaseView {

    /**
     * The presenter for this view. Will be instantiated with
     * {@link #createPresenter()}
     */
    protected P presenter;

    /**
     * Creates a new presenter instance, if needed. Will reuse the previous
     * presenter instance if {@link #setRetainInstance(boolean)} is set to true.
     * This method will be called after from
     * {@link #onViewCreated(android.view.View, android.os.Bundle)}
     */
    protected abstract P createPresenter();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create the presenter if needed
        if (presenter == null) {
            presenter = createPresenter();

            if (presenter == null) {
                throw new NullPointerException("Presenter is null! Do you return null in createPresenter()?");
            }
        }
        presenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        int layoutRes = getLayoutRes();
        if (layoutRes == 0) {
            throw new IllegalArgumentException("getLayoutRes() returned 0, which is not allowed. "
                    + "If you don't want to use getLayoutRes() but implement your own view for this "
                    + "fragment manually, then you have to override onCreateView();");
        } else {
            View v = inflater.inflate(layoutRes, container, false);
            return v;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        presenter.detachView(getRetainInstance());
    }

    /**
     * Return the layout resource like R.layout.my_layout
     *
     * @return the layout resource or null, if you don't want to have an UI
     */
    protected int getLayoutRes() {
        return 0;
    }

}
