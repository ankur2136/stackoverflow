package com.ankur.stackoverflow.data.respository;

import java.util.List;

import com.ankur.stackoverflow.MyApplication;
import com.ankur.stackoverflow.data.datasource.CloudStore;
import com.ankur.stackoverflow.data.datasource.DatabaseItemSource;
import com.ankur.stackoverflow.data.datasource.ItemDataSource;
import com.ankur.stackoverflow.domain.dto.AnswerItem;
import com.ankur.stackoverflow.domain.dto.QuestionItem;
import com.ankur.stackoverflow.utils.LogUtils;

public class ItemRepository implements ContentRepository<QuestionItem, String> {

    private static final String LOG_TAG   = "ITEM_REPOSITORY";

    ItemDataSource mDatabase = new DatabaseItemSource(MyApplication.getMyApplicationContext());

    ItemDataSource mCloud    = new CloudStore();

    @Override
    public void removeItems(List<QuestionItem> questionItems) {

    }

    @Override
    public void setItem(QuestionItem question) {

    }

    @Override
    public List<AnswerItem> getAnswersForQuestion(int questionId) {
        LogUtils.debugLog(LOG_TAG, questionId + "");
        return mCloud.getAnswersForQuestion(questionId);
    }

    @Override
    public List<QuestionItem> getSearchResult(String query) {
        return mCloud.getSearchResults(query);
    }
}
