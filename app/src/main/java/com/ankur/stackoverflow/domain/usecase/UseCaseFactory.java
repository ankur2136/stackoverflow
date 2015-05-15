package com.ankur.stackoverflow.domain.usecase;


import com.ankur.stackoverflow.domain.dto.QuestionItem;
import com.ankur.stackoverflow.domain.interactor.GetItemUseCase;
import com.ankur.stackoverflow.executor.ExecutorFactory;

public class UseCaseFactory {

    public static GetItemUseCase<QuestionItem> newGetQuestionsItemUseCaseInstance() {
        return null;
//        return new GetQuestionItemUseCase(RepositoryFactory.getSongRepositoryInstance(),
//                RepositoryFactory.getSongContentObserverInstance(), ExecutorFactory.getThreadExecutorInstance());
    }
}
