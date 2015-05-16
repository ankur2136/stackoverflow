package com.ankur.stackoverflow.data.datasource;

import com.ankur.stackoverflow.MyApplication;
import com.ankur.stackoverflow.domain.dto.AnswerItem;
import com.ankur.stackoverflow.domain.dto.QuestionItem;
import com.ankur.stackoverflow.utils.RequestUtils;

import java.util.ArrayList;
import java.util.List;

public class CloudStore implements ItemDataSource<QuestionItem> {

    private String LOG_TAG = "CLOUD_STORE";

    @Override
    public List<QuestionItem> getSearchResults(String query) {
        List<QuestionItem> results = new ArrayList<>();
        results = RequestUtils.getSearchResults(MyApplication.getMyApplicationContext(), query);
        return results;
    }

    @Override
    public List<AnswerItem> getAnswersForQuestion(Integer questionId) {
        List<AnswerItem> results = new ArrayList<>();
        results = RequestUtils.getAnswersListForQuestion(MyApplication.getMyApplicationContext(), questionId);
        return results;
    }
}
