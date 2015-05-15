package com.ankur.stackoverflow.domain.usecase;

public interface Callback<T> {

    void onSuccess(T obj);

    void onError(Exception ex);
}
