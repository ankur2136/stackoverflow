package com.ankur.stackoverflow.data.datasource;

import com.ankur.stackoverflow.domain.dto.QuestionItem;

import java.util.ArrayList;
import java.util.List;

public class CloudStore implements ItemDataSource<QuestionItem> {

    private String LOG_TAG = "CLOUD_STORE";

    @Override
    public List<QuestionItem> getSearchResults(String query) {
        QuestionItem dummy = new QuestionItem();
        dummy.mQuestionId = 1;
        dummy.mTitle   = "test";
        dummy.mLink = "google.com";

        List<QuestionItem> results = new ArrayList<>();
        results.add(dummy);

        return results;
    }
}
