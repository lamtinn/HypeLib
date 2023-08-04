package me.lamtinn.hypelib.utils;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Predicate;

public class ValueUtils {

    public static <T> T validate(final boolean expression, T valueIfTrue, T valueIfFalse) {
        return expression ? valueIfTrue : valueIfFalse;
    }

    public static <T> T getRandom(@NotNull final HashSet<T> set) {
        if (set.isEmpty()) return null;

        ArrayList<T> list = new ArrayList<>(set);
        Random random = new Random();
        int index = random.nextInt(list.size());

        return list.get(index);
    }

    public static <T> T getRandom(@NotNull final List<T> list) {
        if (list.isEmpty()) return null;
        Random random = new Random();
        int index = random.nextInt(list.size());

        return list.get(index);
    }

    public static <T> T getRandom(@NotNull final Collection<T> collection) {
        if (collection.isEmpty()) return null;

        ArrayList<T> list = new ArrayList<>(collection);
        Random random = new Random();
        int index = random.nextInt(list.size());

        return list.get(index);
    }

    public static <T> List<T> listFilter(@NotNull final List<T> list, Predicate<T> predicate) {
        List<T> filtered = new ArrayList<>();

        list.forEach(t -> {
            if (predicate.test(t)) {
                filtered.add(t);
            }
        });

        return filtered;
    }

    public static int isInteger(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static long isLong(String input) {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            return  -1L;
        }
    }

    public static BigInteger isBigInt(String input) {
        try {
            return new BigInteger(input);
        } catch (NumberFormatException e) {
            return BigInteger.ZERO;
        }
    }

    public static boolean isInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
