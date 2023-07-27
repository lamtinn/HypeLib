package me.lamtinn.hypelib.utils;

public class ValueUtils {

    public static <T> T validate(final boolean expression, T valueIfTrue, T valueIfFalse) {
        return expression ? valueIfTrue : valueIfFalse;
    }
}
