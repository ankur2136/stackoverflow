package com.ankur.stackoverflow.utils;

public interface ItemCallback<T> {
    Object convert(T t);
}