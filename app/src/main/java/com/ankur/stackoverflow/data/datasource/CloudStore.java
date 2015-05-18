package com.ankur.stackoverflow.data.datasource;

import com.ankur.stackoverflow.MyApplication;
import com.ankur.stackoverflow.domain.dto.AnswerItem;
import com.ankur.stackoverflow.domain.dto.QuestionItem;
import com.ankur.stackoverflow.utils.RequestUtils;

import java.util.List;

public class CloudStore implements ItemDataSource<QuestionItem> {

    @Override
    public List<QuestionItem> getSearchResults(String query) {
        List<QuestionItem> results;
        results = RequestUtils.getSearchResults(MyApplication.getMyApplicationContext(), query);
        return results;
    }

    @Override
    public List<AnswerItem> getAnswersForQuestion(Integer questionId) {
        List<AnswerItem> results;
        results = RequestUtils.getAnswersListForQuestion(MyApplication.getMyApplicationContext(), questionId);
        return results;
    }

    @Override
    public boolean putQuestionItem(QuestionItem item, String Query) {
        return false;
    }

    @Override
    public boolean putAnswerItem(AnswerItem item) {
        return false;
    }
}
