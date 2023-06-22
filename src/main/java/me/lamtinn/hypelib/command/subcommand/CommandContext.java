package me.lamtinn.hypelib.command.subcommand;

import me.lamtinn.hypelib.command.interfaces.HCommandContext;
import me.lamtinn.hypelib.utils.AdventureUtils;
import me.lamtinn.hypelib.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class CommandContext implements HCommandContext {

    private final CommandSender sender;
    private final String label;
    private final String[] args;

    public CommandContext(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String[] args) {
        this.sender = sender;
        this.label = label;
        this.args = args;
    }

    @Override
    public @Nullable Player getAsPlayer() {
        return sender instanceof Player ? ((Player) sender).getPlayer() : null;
    }

    @Override
    public int argsLength() {
        return this.args.length;
    }

    @Override
    public void sendMessage(@Nullable String msg) {
        AdventureUtils.sendMessage(sender, msg);
    }

    @Override
    public void sendMessage(@Nullable String msg, Consumer<Void> consumer) {
        sendMessage(msg);
        consumer.accept(null);
    }

    @Override
    public void sendMessage(@NotNull Player player, @Nullable String msg) {
        AdventureUtils.playerMessage(player, msg);
    }

    @Override
    public void sendMessage(@NotNull Player player, @Nullable String msg, Consumer<Player> consumer) {
        sendMessage(player, msg);
        consumer.accept(player);
    }

    @Override
    public void sendActionBar(@Nullable String msg) {
        AdventureUtils.playerActionbar(getAsPlayer(), msg);
    }

    @Override
    public void sendActionBar(@Nullable String msg, Consumer<Player> consumer) {
        sendActionBar(msg);
        consumer.accept(getAsPlayer());
    }

    @Override
    public String getName() {
        return isConsole() ? "Console" : getAsPlayer().getName();
    }

    @Override
    public String getString(int index) {
        return getString(index, null);
    }

    @Override
    public String getString(int index, String defaultValue) {
        return this.args.length <= index ? defaultValue : this.args[index];
    }

    @Override
    public int getInt(int index) {
        return getInt(index, 0);
    }

    @Override
    public int getInt(int index, int defaultValue) {
        if (this.args.length <= index) return defaultValue;
        try {
            return Integer.parseInt(this.args[index]);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public long getLong(int index) {
        return getLong(index, 0L);
    }

    @Override
    public long getLong(int index, long defaultValue) {
        if (this.args.length <= index) return defaultValue;
        try {
            return Long.parseLong(this.args[index]);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public double getDouble(int index) {
        return getDouble(index, 0.0);
    }

    @Override
    public double getDouble(int index, double defaultValue) {
        if (this.args.length <= index) return defaultValue;
        try {
            return Double.parseDouble(this.args[index]);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public @Nullable Player getPlayer(int index) {
        return this.args.length <= index ? null : PlayerUtils.byName(this.args[index]);
    }

    @Override
    public boolean getBool(int index) {
        return getBool(index, false);
    }

    @Override
    public boolean getBool(int index, boolean defaultValue) {
        if (this.args.length <= index) return defaultValue;
        try {
            return Boolean.parseBoolean(this.args[index]);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public boolean isConsole() {
        return (sender instanceof ConsoleCommandSender);
    }

    @Override
    public boolean hasPermission(@NotNull String perm) {
        if (!(sender instanceof Player player)) return true;
        return player.isOp() || player.hasPermission("*") || player.hasPermission(perm);
    }

    @Override
    public boolean hasPermission(Player player, @NotNull String perm) {
        return player.isOp() || player.hasPermission("*") || player.hasPermission(perm);
    }

    @Override
    public @NotNull CommandSender getSender() {
        return this.sender;
    }

    @Override
    public @NotNull String getLabel() {
        return this.label;
    }

    @Override
    public @NotNull String[] getArgs() {
        return this.args;
    }
}
