package com.ankur.stackoverflow.presentation.presenter;

import com.ankur.stackoverflow.presentation.view.BaseView;

public interface BasePresenter<V extends BaseView> {

    /**
     * Set or attach the view to this presenter
     */
    public void attachView(V view);

    /**
     * Will be called if the view has been destroyed. Typically this method will be invoked from
     * <code>Activity.detachView()</code> or <code>Fragment.onDestroyView()</code>
     */
    public void detachView(boolean retainInstance);
}
