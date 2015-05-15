package com.ankur.stackoverflow.domain.interactor;


import com.ankur.stackoverflow.common.QueryParams;
import com.ankur.stackoverflow.domain.observer.ContentObserver;
import com.ankur.stackoverflow.domain.usecase.Callback;
import com.ankur.stackoverflow.executor.PostExecutionThread;

public interface GetItemsUseCase<T> extends Interactor, ContentObserver.ContentListener {

    void getItem(QueryParams queryParams, PostExecutionThread postExecutionThread, Callback<T> callback, boolean async,
            boolean applyUserState);

    void removeListener();
}
