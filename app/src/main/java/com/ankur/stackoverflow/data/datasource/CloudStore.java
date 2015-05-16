package com.ankur.stackoverflow.data.datasource;

import com.ankur.stackoverflow.MyApplication;
import com.ankur.stackoverflow.domain.dto.QuestionItem;
import com.ankur.stackoverflow.utils.QuestionRequestUtils;

import java.util.ArrayList;
import java.util.List;

public class CloudStore implements ItemDataSource<QuestionItem> {

    private String LOG_TAG = "CLOUD_STORE";

    @Override
    public List<QuestionItem> getSearchResults(String query) {
        List<QuestionItem> results = new ArrayList<>();
        results = QuestionRequestUtils.getSearchResults(MyApplication.getMyApplicationContext(), query);
        return results;
    }
}
