package org.auraclient.auraclient.cloaks.utils;

import java.util.Random;

public class RandomUtils {
    private static String random(final int length, final char[] chars) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++)
            stringBuilder.append(chars[new Random().nextInt(chars.length)]);
        return stringBuilder.toString();
    }

    private static String random(final int length, final String chars) {
        return random(length, chars.toCharArray());
    }

    public static String randomString(final int length) {
        return random(length, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }

    public static String randomStringLowercase(final int length) {
        return random(length, "0123456789abcdefghijklmnopqrstuvwxyz");
    }

    public static int randBetween(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }
}