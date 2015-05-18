package com.ankur.stackoverflow.data.respository;

import java.util.List;

public interface ContentRepository<T, K> {

    /**
     * Get question collection item by question type. Collection is present as child
     * items
     *
     * @param questionId = question
     * @return List<AnswerItem>
     */
    List<K> getAnswersForQuestion(int questionId);


    /**
     * Get search results for query
     *
     * @param query = query
     * @return List<QuestionItem>
     */
    List<T> getSearchResult(String query);

}
