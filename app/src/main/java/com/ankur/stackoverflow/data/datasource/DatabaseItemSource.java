package com.ankur.stackoverflow.data.datasource;

import android.content.Context;

import com.ankur.stackoverflow.data.datasource.database.IItemDataSource;
import com.ankur.stackoverflow.domain.dto.QuestionItem;

import java.util.List;

public class DatabaseItemSource implements ItemDataSource<QuestionItem> {

    IItemDataSource mMediaDataSource;

    public DatabaseItemSource(Context context) {
        mMediaDataSource = new com.ankur.stackoverflow.data.datasource.database.ItemDataSource(context);
    }


    @Override
    public List<QuestionItem> getSearchResults(String query) {
        return null;
    }
}
