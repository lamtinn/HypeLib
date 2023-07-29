package me.lamtinn.hypelib.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

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
}
