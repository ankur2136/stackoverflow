package com.ankur.stackoverflow.domain.usecase;

import com.ankur.stackoverflow.common.QueryParams;
import com.ankur.stackoverflow.data.respository.ContentRepository;
import com.ankur.stackoverflow.domain.dto.QuestionItem;
import com.ankur.stackoverflow.domain.interactor.GetItemsUseCase;
import com.ankur.stackoverflow.executor.PostExecutionThread;
import com.ankur.stackoverflow.executor.ThreadExecutor;
import com.ankur.stackoverflow.utils.LogUtils;

import java.util.List;

public class GetQuestionItemsUseCase extends BaseUseCase implements GetItemsUseCase<List<QuestionItem>> {

    private static final String     LOG_TAG = "GET_QUESTION_ITEMS_USE_CASE";

    private final ContentRepository mContentRepository;

    private String                  mQueryText;

    public GetQuestionItemsUseCase(ContentRepository contentRepository, ThreadExecutor executor) {
        mContentRepository = contentRepository;
        mThreadExecutor = executor;
    }

    @Override
    public void getItem(QueryParams queryParams, PostExecutionThread postExecutionThread, Callback callback,
            boolean async, boolean applyUserState) {
        mQueryText = queryParams.getTEXT();
        mCallback = callback;
        mPostExecutionThread = postExecutionThread;
        mAsync = async;
        mApplyUserState = applyUserState;
        getData();
    }

    @Override
    public void run() {
        try {
            List<QuestionItem> result = mContentRepository.getSearchResult(mQueryText);
            notifyOnSuccess(result);
        } catch (Exception e) {
            LogUtils.errorLog(LOG_TAG, "Exception on background thread... ", e);
            notifyOnError(e);
        }
    }

    private void getData() {
        if (mAsync) {
            if (!isTaskRunning()) {
                mFuture = mThreadExecutor.execute(this);
            }
        } else {
            run();
        }
    }
}
