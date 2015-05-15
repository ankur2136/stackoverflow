package com.ankur.stackoverflow.data.respository;


import com.ankur.stackoverflow.data.proxy.CachingProxy;

public class RepositoryFactory {

    private static ContentRepository sQuestionRepository;

    public static synchronized ContentRepository getQuestionRepositoryInstance() {
        if (sQuestionRepository == null) {
            sQuestionRepository = (ContentRepository) CachingProxy.newInstance(new ItemRepository());
        }
        return sQuestionRepository;
    }

}
