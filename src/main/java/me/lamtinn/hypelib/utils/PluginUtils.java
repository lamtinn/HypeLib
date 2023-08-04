package me.lamtinn.hypelib.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PluginUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&(#[a-f0-9]{6})", Pattern.CASE_INSENSITIVE);

    public static @NotNull String color(@NotNull String input) {
        Matcher m = HEX_PATTERN.matcher(input);
        while (m.find()) input = input.replace(m.group(), ChatColor.valueOf(m.group(1)).toString());
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static @NotNull String getDate(String input) {
        return new SimpleDateFormat(input).format(new Date());
    }

    public static void isDependency(@NotNull String name, @NotNull Consumer<Void> callback) {
        if (Bukkit.getPluginManager().getPlugin(name) != null) callback.accept(null);
    }

    public static void hookDependency(@NotNull String name, @NotNull Consumer<Void> callback) {
        if (Bukkit.getPluginManager().getPlugin(name) == null) return;
        callback.accept(null);
    }

    public static String getPluginVer(String name) {
        return "v" + Bukkit.getPluginManager().getPlugin(name).getDescription().getVersion();
    }
}
