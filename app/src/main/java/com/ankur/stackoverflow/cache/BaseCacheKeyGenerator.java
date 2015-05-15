package com.ankur.stackoverflow.cache;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.List;

public class BaseCacheKeyGenerator {
    public static final int NO_PARAM_KEY = 0;
    public static final int NULL_PARAM_KEY = 53;

    public static String generate(Object target, Method method, Object... params) {
        if (params.length == 0) {
            return String.format("%s_%s_%s", target.getClass().getSimpleName(), method.getName(), NO_PARAM_KEY);
        }
        int hashCode = 17;
        for (Object object : params) {
            hashCode = 31 * hashCode + (object == null ? NULL_PARAM_KEY : object.hashCode());
        }
        return String.format("%s_%s_%s", target.getClass().getSimpleName(), method.getName(), Integer.valueOf(hashCode));
    }

    /**
     * Create an MD5 hash of a string.
     *
     * @param input Input string.
     * @return Hash of input.
     * @throws IllegalArgumentException if {@code input} is blank.
     */
    public static String md5(String input) {
        if (input == null || input.length() == 0) {
            throw new IllegalArgumentException("Input string must not be blank.");
        }
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(input.getBytes());
            byte[] messageDigest = algorithm.digest();

            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString((messageDigest[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Cannot generate md5 for string [%s]", input));
        }
    }
}
