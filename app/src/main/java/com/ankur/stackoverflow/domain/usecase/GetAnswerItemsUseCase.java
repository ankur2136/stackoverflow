package com.ankur.stackoverflow.domain.usecase;

import com.ankur.stackoverflow.common.QueryParams;
import com.ankur.stackoverflow.data.respository.ContentRepository;
import com.ankur.stackoverflow.domain.dto.AnswerItem;
import com.ankur.stackoverflow.domain.interactor.GetItemsUseCase;
import com.ankur.stackoverflow.executor.PostExecutionThread;
import com.ankur.stackoverflow.executor.ThreadExecutor;
import com.ankur.stackoverflow.utils.LogUtils;

import java.util.List;

public class GetAnswerItemsUseCase extends BaseUseCase implements GetItemsUseCase<List<AnswerItem>> {

    private static final String     LOG_TAG = "GET_ANSWER_ITEMS_USE_CASE";

    private final ContentRepository mContentRepository;

    private Integer                 mQuestionId;

    public GetAnswerItemsUseCase(ContentRepository contentRepository, ThreadExecutor executor) {
        mContentRepository = contentRepository;
        mThreadExecutor = executor;
    }

    @Override
    public void getItem(QueryParams queryParams, PostExecutionThread postExecutionThread, Callback callback,
            boolean async, boolean applyUserState) {
        mQuestionId = queryParams.getId();
        mCallback = callback;
        mPostExecutionThread = postExecutionThread;
        mAsync = async;
        mApplyUserState = applyUserState;
        getData();
    }

    @Override
    public void removeListener() {
        mCallback = null;
    }

    @Override
    public void run() {
        try {
            List<AnswerItem> result = mContentRepository.getAnswersForQuestion(mQuestionId);
            notifyOnSuccess(result);
        } catch (Exception e) {
            LogUtils.errorLog(LOG_TAG, "Exception on background thread... ", e);
            notifyOnError(e);
        }
    }

    @Override
    public void onContentChanged(String contentId) {
        // Content changed, get latest list again
        getData();
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
