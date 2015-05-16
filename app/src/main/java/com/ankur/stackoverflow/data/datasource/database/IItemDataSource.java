package com.ankur.stackoverflow.data.datasource.database;


import com.ankur.stackoverflow.domain.dto.AnswerItem;
import com.ankur.stackoverflow.domain.dto.QuestionItem;

import java.util.List;

public interface IItemDataSource {

    List<QuestionItem> getSearchResults(String input);

    List<AnswerItem> getAnswersForQuestion(Integer questionID);

    boolean putQuestionItem(QuestionItem questionItem, String query);
}
