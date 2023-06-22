package me.lamtinn.hypelib.utils;

import org.bukkit.ChatColor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PluginUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&(#[a-f0-9]{6})", Pattern.CASE_INSENSITIVE);

    public static String color(String input) {
        Matcher m = HEX_PATTERN.matcher(input);
        while (m.find()) input = input.replace(m.group(), ChatColor.valueOf(m.group(1)).toString());
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String getDate(String input) {
        return new SimpleDateFormat(input).format(new Date());
    }

    public static int isInteger(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
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
