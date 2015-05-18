package com.ankur.stackoverflow.utils;

import java.util.Collection;

public final class CollectionUtils {

    private CollectionUtils() {
        throw new RuntimeException("Should not be instantiated");
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }

}
