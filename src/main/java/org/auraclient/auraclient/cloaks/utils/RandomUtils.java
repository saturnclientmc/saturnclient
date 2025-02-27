package org.auraclient.auraclient.cloaks.utils;

import java.util.Random;

/**
 * Utility class for generating random strings and numbers.
 */
public final class RandomUtils {
    private static final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String ALPHANUMERIC_LOWERCASE = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final Random RANDOM = new Random();

    private RandomUtils() {
        // Prevent instantiation
    }

    /**
     * Generates a random string of specified length using given characters.
     */
    private static String random(final int length, final char[] chars) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars[RANDOM.nextInt(chars.length)]);
        }
        return sb.toString();
    }

    /**
     * Generates a random string of specified length using alphanumeric characters.
     */
    public static String randomString(final int length) {
        return random(length, ALPHANUMERIC.toCharArray());
    }

    /**
     * Generates a random lowercase string of specified length.
     */
    public static String randomStringLowercase(final int length) {
        return random(length, ALPHANUMERIC_LOWERCASE.toCharArray());
    }

    /**
     * Generates a random integer between min and max (inclusive).
     */
    public static int randBetween(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }
}