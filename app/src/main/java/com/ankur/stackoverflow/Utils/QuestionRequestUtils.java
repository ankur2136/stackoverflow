package com.ankur.stackoverflow.utils;

import android.content.Context;

import com.ankur.stackoverflow.common.ApiUtils;
import com.ankur.stackoverflow.data.entity.SearchResponse;
import com.ankur.stackoverflow.domain.dto.QuestionItem;

import java.util.List;

public class QuestionRequestUtils extends ApiUtils {

    private static final String LOG_TAG = "QUESTION_REQUEST_UTILS";

    public static List<QuestionItem> getSearchResults(Context context, String query) {
        String url = "https://api.stackexchange.com/2.2/search?order=desc&sort=activity&tagged=facebook&site=stackoverflow";
        final ResultWrapper resultWrapper = new ResultWrapper();
        makeSyncGetJsonRequest(new SearchResponse(), context, url, 10000, new ApiResponseListener<SearchResponse>() {
            @Override
            public void onResponse(SearchResponse searchResponse) {
                LogUtils.debugLog(LOG_TAG, "Results received");
                resultWrapper.setResult(searchResponse.getSearchResults());
            }

            @Override
            public void onError(Exception exception) {
                LogUtils.errorLog(LOG_TAG, "Failed to fetch top page. API returned: " + exception);
            }

        });
        return (List<QuestionItem>) resultWrapper.getResult();
    }

}
