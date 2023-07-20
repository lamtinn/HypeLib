package me.lamtinn.hypelib.command.interfaces;

import me.lamtinn.hypelib.command.subcommand.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface HSubCommandManager {
    boolean onCommand(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String[] args);

    List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final String label, @NotNull final String[] args);

    void onHelp(@NotNull final Consumer<CommandSender> consumer);

    void onCommandNotFound(@NotNull final BiConsumer<CommandSender, String> consumer);

    void onNoPermission(@NotNull final BiConsumer<CommandSender, SubCommand> consumer);

    void onMissingArgs(@NotNull final BiConsumer<CommandSender, SubCommand> consumer);

    void onNotConsole(@NotNull final BiConsumer<Player, SubCommand> consumer);

    void onNotPlayer(@NotNull final BiConsumer<CommandSender, SubCommand> consumer);

    void onExecuteError(@NotNull final BiConsumer<CommandSender, String> consumer);

    void register(@NotNull final SubCommand subCommand);

    void unregister(@NotNull final SubCommand subCommand);

    void unregisterAll();

    void setHelpPerm(@NotNull final String perm);

    Map<String, SubCommand> getSubCommands();

    SubCommand getSubCommand(@NotNull final String name);
}
