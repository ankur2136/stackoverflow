package com.ankur.stackoverflow.data.datasource;

import com.ankur.stackoverflow.domain.dto.QuestionItem;

import java.util.List;


public interface ItemDataSource<T> {

    List<QuestionItem> getSearchResults(String query);
}
