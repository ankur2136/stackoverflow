package com.ankur.stackoverflow.presenter.fragment.view;

public interface BaseView {

    /*
     * Interaction listener to be implemented by the Activity
     */
    interface InteractionListener<T> {
        void onItemClick(T item);
    }
}
