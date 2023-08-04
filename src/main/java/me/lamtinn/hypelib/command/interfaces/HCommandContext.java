package me.lamtinn.hypelib.command.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface HCommandContext {
    @Nullable Player getAsPlayer();

    int argsLength();

    void sendMessage(@Nullable String msg);

    void sendMessage(@Nullable String msg, final Consumer<Void> consumer);

    void sendMessage(@NotNull Player player, @Nullable String msg);

    void sendMessage(@NotNull Player player, @Nullable String msg, final Consumer<Player> consumer);

    void sendActionBar(@Nullable String msg);

    void sendActionBar(@Nullable String msg, final Consumer<Player> consumer);

    String getName();

    String getString(int index);

    String getString(int index, String defaultValue);

    int getInt(int index);

    int getInt(int index, int defaultValue);

    long getLong(int index);

    long getLong(int index, long defaultValue);

    double getDouble(int index);

    double getDouble(int index, double defaultValue);

    @Nullable Player getPlayer(int index);

    boolean getBool(int index);

    boolean getBool(int index, boolean defaultValue);

    boolean isConsole();

    boolean hasPermission(@NotNull String perm);

    boolean hasPermission(@NotNull Permission permission);

    boolean hasPermission(final Player player, @NotNull String perm);

    @NotNull CommandSender getSender();

    @NotNull String getLabel();

    @NotNull String[] getArgs();
}
