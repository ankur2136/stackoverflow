package com.ankur.stackoverflow.presentation.view;

import java.util.Collection;

import android.view.View;

/**
 * Interface representing a view that will load a collection.
 */
public interface CollectionView<T> extends LoadDataView {

    // /*
    // * Render collection loaded by the presenter
    // */
    void renderCollection(Collection<T> collection);

    void renderItem(T item);

    /*
     * View the selected item from the collection
     */
    void viewItem(T item);

    void deleteItem(T item);

    void viewPopupWindow(View v, T item);
}
