package com.ankur.stackoverflow.domain.observer;

import java.util.List;

public interface ContentObserver<T> {

    interface ContentListener<T> {
        void onContentChanged(String contentId);
    }

    void addListener(String contentId, ContentListener listener);

    void removeListener(ContentListener listener);

    void notify(String contentId);

    void notify(List<String> contentIds);
}
