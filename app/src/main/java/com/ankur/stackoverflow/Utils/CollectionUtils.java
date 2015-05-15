package com.ankur.stackoverflow.utils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CollectionUtils {

    private CollectionUtils() {
        throw new RuntimeException("Should not be instantiated");
    }

    public static <T> boolean isEmpty(T[] array) {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Collection collection) {
        if (collection == null || collection.size() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Map map) {
        if (map == null || map.size() == 0) {
            return true;
        }
        return false;
    }

    public static <R, T> Map<R, T> collectionToMapWithCollItemAsValue(Collection<T> coll, ItemCallback<T> callback) {
        if (callback == null) {
            throw new RuntimeException("Callback for getting key cannot be null");
        }
        Map<R, T> map = new LinkedHashMap();
        for (T t : coll) {
            map.put((R) callback.convert(t), t);
        }
        return map;
    }

    public static <T, R> Map<T, R> collectionToMapWithCollItemAsKey(Collection<T> coll, ItemCallback<T> callback) {
        if (callback == null) {
            throw new RuntimeException("Callback for getting value cannot be null");
        }
        Map<T, R> map = new LinkedHashMap();
        for (T t : coll) {
            map.put(t, (R) callback.convert(t));
        }
        return map;
    }

    public static <T> String join(Iterator<T> itr, String separator, ItemCallback callback) {
        if (itr == null || !itr.hasNext()) {
            return "";
        }

        StringBuilder b = new StringBuilder();
        int count = 0;
        while (itr.hasNext()) {
            Object item = null;
            if (callback != null) {
                item = callback.convert(itr.next());
            } else {
                item = itr.next();
            }
            if (count > 0) {
                b.append(separator);
            }
            b.append(String.valueOf(item));
            count++;
        }
        return b.toString();
    }

    public static <T> String join(Collection<T> collection, String separator, ItemCallback callback) {
        if (collection == null) {
            return "";
        }

        int iMax = collection.size() - 1;
        if (iMax == -1) {
            return "";
        }

        StringBuilder b = new StringBuilder();
        Iterator<T> itr = collection.iterator();

        int count = 0;
        while (itr.hasNext()) {
            Object item = null;
            if (callback != null) {
                item = callback.convert(itr.next());
            } else {
                item = itr.next();
            }
            if (count > 0) {
                b.append(separator);
            }
            b.append(String.valueOf(item));
            count++;
        }
        return b.toString();
    }

    public static <T> String join(T[] array, String separator, ItemCallback callback) {
        if (array == null) {
            return "";
        }

        int iMax = array.length - 1;
        if (iMax == -1) {
            return "";
        }

        StringBuilder b = new StringBuilder();
        int count = 0;
        for (T t : array) {
            Object item = null;
            if (callback != null) {
                item = callback.convert(t);
            } else {
                item = t;
            }
            if (count > 0) {
                b.append(separator);
            }
            b.append(String.valueOf(item));
            count++;
        }
        return b.toString();
    }

    public static <K, V> String join(Map<K, V> map, String separator, ItemCallback callback) {
        if (map == null) {
            return "";
        }

        int iMax = map.size() - 1;
        if (iMax == -1) {
            return "";
        }

        StringBuilder b = new StringBuilder();
        int count = 0;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            Object item = null;
            if (callback != null) {
                item = callback.convert(entry);
            } else {
                item = entry;
            }
            if (count > 0) {
                b.append(separator);
            }
            b.append(String.valueOf(item));
            count++;
        }
        return b.toString();
    }

    public static Object[][] arrayChunk(Object[] array, int size) {
        Object[][] target = new Object[(array.length + size - 1) / size][];

        for (int i = 0; i < target.length; i++) {
            int innerArraySize = array.length - i * size >= size ? size : array.length - i * size;
            Object[] inner = new Object[innerArraySize];
            System.arraycopy(array, i * size, inner, 0, innerArraySize);
            target[i] = inner;
        }

        return target;
    }

    public static <T> List<T> slice(List<T> list, int startIndex, int endIndex) {
        if (isEmpty(list)) {
            return Collections.emptyList();
        }
        if (endIndex > list.size()) {
            endIndex = list.size();
        }
        if (startIndex > endIndex) {
            return Collections.emptyList();
        }
        return list.subList(startIndex, endIndex);
    }

    public static <T> List<T> pickRandomList(List<T> list, int num) {
        if (isEmpty(list)) {
            return Collections.emptyList();
        }

        int numItems = Math.min(list.size(), num);
        List<T> copyList = new ArrayList<T>(list);
        Collections.shuffle(copyList);
        return copyList.subList(0, numItems);
    }

    public static Object[] pickRandomArray(Object[] arr, int num) {
        return pickRandomList(Arrays.asList(arr), num).toArray();
    }

    public static List<String> stringToList(String csvString, boolean lowerCase) {
        if (csvString == null) {
            return null;
        }

        String[] tokens = null;
        if (lowerCase) {
            tokens = csvString.toLowerCase().split(",");
        } else {
            tokens = csvString.split(",");
        }
        List<String> words = Arrays.asList(tokens);
        return words;
    }

    public static <T> List<T> subList(List<T> list, int offset, int num) {
        if (offset == 0 && num == list.size()) {
            return list;
        }

        int toIndex = offset + num;
        if (toIndex > list.size()) {
            toIndex = list.size();
        }
        return list.subList(offset, toIndex);
    }

    public static JSONArray getJsonArrayFromStringList(List<String> list) {
        JSONArray arr = new JSONArray();
        if (list != null) {
            for (String s : list) {
                arr.put(s);
            }
        }
        return arr;
    }

    public static String joinList(List list, java.lang.String separator) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (Object object : list) {
            buffer.append(object.toString() + separator);
        }
        buffer.delete(buffer.length() - separator.length(), buffer.length());
        return buffer.toString();

    }

    public static String joinSet(Set set, java.lang.String separator) {
        if (set == null || set.size() == 0) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (Object object : set) {
            buffer.append(object.toString() + separator);
        }
        buffer.delete(buffer.length() - separator.length(), buffer.length());
        return buffer.toString();

    }

    public static List gridView(List list, int rows, int columns) {
        List result = new ArrayList();
        List<List> grid = new ArrayList<List>();
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i * columns + j) < list.size()) {
                    if (i >= grid.size()) {
                        grid.add(new ArrayList());
                    }
                    grid.get(i).add(list.get(i * columns + j));
                    count++;
                }
            }
        }
        List leftover = list.subList(count, list.size());
        result.add(grid);
        result.add(leftover);
        return result;
    }

    public static <T> List<T> removeDuplicatesUsingSet(List<T> list) {
        Set<T> set = new LinkedHashSet<>(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    public static String collectionToCommaSeparatedString(Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        boolean isempty = true;
        for (String kw : collection) {
            if (!isempty) {
                buffer.append(',');
            }
            buffer.append(kw);
            isempty = false;
        }
        return buffer.toString();
    }
}
