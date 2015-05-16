package com.ankur.stackoverflow.presentation.presenter;

import android.view.View;

import com.ankur.stackoverflow.common.QueryParams;
import com.ankur.stackoverflow.domain.interactor.GetItemsUseCase;
import com.ankur.stackoverflow.domain.usecase.Callback;
import com.ankur.stackoverflow.executor.ExecutorFactory;
import com.ankur.stackoverflow.presentation.view.CollectionView;
import com.ankur.stackoverflow.utils.LogUtils;

import java.util.Collection;

public class ItemPresenter<I> extends Presenter<CollectionView<I>> {

    private static final String LOG_TAG  = "ITEM_PRESENTER";

    private GetItemsUseCase<I>  mGetItemsUseCase;

    private Callback            mGetCollectionCallback;

    private boolean             isPaused = true;

    public ItemPresenter(GetItemsUseCase<I> getItemsUseCase) {
        mGetItemsUseCase = getItemsUseCase;
        mGetCollectionCallback = new Callback<Collection<I>>() {
            @Override
            public void onSuccess(Collection<I> item) {
                handleItemUpdate(item);
            }

            @Override
            public void onError(Exception exception) {
                handleError(exception);
            }
        };
    }

    /**
     * Handles the collection fetched
     */
    private void handleItemUpdate(Collection<I> item) {
        hideViewLoading();
        showItemInView(item);
    }

    /**
     * Handles the error occurred while fetching the collection
     */
    private void handleError(Exception ex) {
        hideViewLoading();
        showErrorMessage(ex);
        showViewRetry();
    }

    @Override
    public void resume() {
        if (LogUtils.isDebugLogEnabled()) {
            LogUtils.debugLog(LOG_TAG, this.getClass().getSimpleName() + ": resume()");
        }
        isPaused = false;
    }

    @Override
    public void pause() {
        if (LogUtils.isDebugLogEnabled()) {
            LogUtils.debugLog(LOG_TAG, this.getClass().getSimpleName() + ": pause()");
        }
        isPaused = true;
    }

    public void init(QueryParams queryParams) {
        loadItem(queryParams);
    }

    public void loadItem(QueryParams queryParams) {
        hideViewRetry();
        showViewLoading();
        getItem(queryParams);
    }

    private void getItem(QueryParams queryParams) {
        mGetItemsUseCase.getItem(queryParams, ExecutorFactory.getPostExecutionThreadInstance(), mGetCollectionCallback,
                true, true);
    }

    void showItemInView(Collection<I> item) {
        if (isPaused) {
            return;
        }
        getView().renderCollection(item);
    }

    private void showViewLoading() {
        getView().showLoading();
    }

    private void hideViewLoading() {
        getView().hideLoading();
    }

    private void showViewRetry() {
        getView().showRetry();
    }

    private void hideViewRetry() {
        getView().hideRetry();
    }

    public void onItemClicked(I item) {
        getView().viewItem(item);
    }

    public void onOverflowClicked(View v, I item) {
    }

    private void showErrorMessage(Exception ex) {
        if (ex != null) {
            getView().showError(ex.toString());
        } else {
            getView().showError("NULL ERROR");
        }
    }

}
