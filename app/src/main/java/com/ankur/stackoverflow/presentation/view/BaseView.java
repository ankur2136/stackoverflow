package com.ankur.stackoverflow.presentation.view;

public interface BaseView {

    /*
     * Interaction listener to be implemented by the Activity
     */
    interface InteractionListener<T> {
        void onItemClick(T item);
    }
}
