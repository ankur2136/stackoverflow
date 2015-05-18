package com.ankur.stackoverflow.presentation.presenter;

import com.ankur.stackoverflow.presentation.view.BaseView;

import java.lang.ref.WeakReference;

/**
 * Interface representing a Presenter in a model view presenter (MVP) pattern.
 */
public class Presenter<V extends BaseView> implements BasePresenter<V> {

    private WeakReference<V> viewRef;

    @Override
    public void attachView(V view) {
        viewRef = new WeakReference<>(view);
    }

    /**
     * Get the attached view. You should always call {@link #isViewAttached()}
     * to check if the view is attached to avoid NullPointerExceptions
     */
    protected V getView() {
        return viewRef.get();
    }

    protected void resume() {
    }

    protected void pause() {
    }

    /**
     * Checks if a view is attached to this presenter. You should always call
     * this method before calling {@link #getView()} to get the view instance.
     */
    protected boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    @Override
    public void detachView(boolean retainInstance) {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }
}
