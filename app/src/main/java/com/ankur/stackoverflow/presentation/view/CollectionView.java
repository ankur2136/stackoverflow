package com.ankur.stackoverflow.presentation.view;

import java.util.Collection;

/**
 * Interface representing a view that will load a collection.
 */
public interface CollectionView<T> extends LoadDataView {

    // /*
    // * Render collection loaded by the presenter
    // */
    void renderCollection(Collection<T> collection);

    /*
     * View the selected item from the collection
     */
    void viewItem(T item);
}
