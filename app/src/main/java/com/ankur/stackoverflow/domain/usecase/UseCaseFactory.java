package com.ankur.stackoverflow.domain.usecase;

import com.ankur.stackoverflow.data.respository.RepositoryFactory;
import com.ankur.stackoverflow.domain.interactor.GetItemsUseCase;
import com.ankur.stackoverflow.executor.ExecutorFactory;

import java.util.List;

public class UseCaseFactory {

    public static GetItemsUseCase newGetQuestionsItemUseCaseInstance() {
        return new GetQuestionItemsUseCase(RepositoryFactory.getQuestionRepositoryInstance(),
                ExecutorFactory.getThreadExecutorInstance());
    }

    public static GetItemsUseCase newGetAnswersItemUseCaseInstance() {
        return new GetAnswerItemsUseCase(RepositoryFactory.getQuestionRepositoryInstance(),
                ExecutorFactory.getThreadExecutorInstance());
    }
}
