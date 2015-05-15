package com.ankur.stackoverflow.data.respository;

import java.util.List;

import com.ankur.stackoverflow.domain.dto.QuestionItem;

public interface ContentRepository<T, K> {

    /**
     * Remove question items
     *
     * @param questionItems
     */
    void removeItems(List<QuestionItem> questionItems);

    /**
     * Replace question item. It will also replace child items if present
     *
     * @param question
     */
    void setItem(T question);

    /**
     * Get question collection item by question type. Collection is present as child
     * items
     *
     * @param stateList
     * @param questionItemType
     * @return
     */
    /**
     * Get search results for query
     *
     * @param query
     * @return
     */
    List<QuestionItem> getSearchResult(String query);

}