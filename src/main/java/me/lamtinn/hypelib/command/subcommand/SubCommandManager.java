package me.lamtinn.hypelib.command.subcommand;

import me.lamtinn.hypelib.command.annotations.Command;
import me.lamtinn.hypelib.command.interfaces.HSubCommandManager;
import me.lamtinn.hypelib.utils.AdventureUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SubCommandManager implements HSubCommandManager {

    protected static final String HELP = "help";
    private final Map<String, SubCommand> subcommands = new HashMap<>();

    private Consumer<CommandSender> onHelp = (sender -> {});
    private BiConsumer<CommandSender, String> onCmdNotFound = ((sender, str) -> AdventureUtils.sendMessage(sender, "<gray>Command with name <#FF416C>'" + str + "' <gray>doesn't exist!"));
    private BiConsumer<CommandSender, SubCommand> noPerm = ((sender, subCommand) -> AdventureUtils.sendMessage(sender, "<gray>You don''t have permission to do that! <#FF416C>(Required: " + subCommand.getPermission() + ")"));
    private BiConsumer<CommandSender, SubCommand> onMissingArgs = ((sender, subCommand) -> AdventureUtils.sendMessage(sender, "<gray>Missing parameters! Use: <#FF416C>" + subCommand.getUsage() + "<gray>."));
    private BiConsumer<CommandSender, SubCommand> notPlayer = ((sender, subCommand) -> AdventureUtils.sendMessage(sender, "<gray>Only players can use this command!"));
    private BiConsumer<Player, SubCommand> onNotConsole = ((player, subCommand) -> AdventureUtils.playerMessage(player, "<gray>This command can only be used on console!"));
    private BiConsumer<CommandSender, String> onExecuteError = ((sender, str) -> AdventureUtils.sendMessage(sender, "<gray>An error occurred when using <#FF416C>" + str + " <gray>command. Check your console..."));

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1 || args[0].equalsIgnoreCase(HELP)) {
            this.onHelp.accept(sender);
            return true;
        }

        SubCommand cmd = this.getSubCommand(args[0]);
        if (cmd == null) {
            this.onCmdNotFound.accept(sender, args[0].toLowerCase());
            return true;
        }

        if (!hasPermission(sender, cmd.getPermission())) {
            this.noPerm.accept(sender, cmd);
            return true;
        }

        switch (cmd.getCommand().target()) {
            case ONLY_PLAYER -> {
                if (!(sender instanceof Player)) {
                    this.notPlayer.accept(sender, cmd);
                    return true;
                }
            }
            case ONLY_CONSOLE -> {
                if (!(sender instanceof ConsoleCommandSender)) {
                    this.onNotConsole.accept((Player) sender, cmd);
                    return true;
                }
            }
        }

        int min = cmd.getCommand().minArgs();
        if (min > 0) {
            if (args.length < min) {
                this.onMissingArgs.accept(sender, cmd);
                return true;
            }
        }

        try {
            CommandContext context = new CommandContext(sender, label, Arrays.copyOfRange(args, 1, args.length));
            cmd.onCommand(context);
            return true;
        } catch (IOException e) {
            this.onExecuteError.accept(sender, args[0].toLowerCase());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        return null;
    }

    @Override
    public void onHelp(@NotNull Consumer<CommandSender> consumer) {
        this.onHelp = consumer;
    }

    @Override
    public void onCommandNotFound(@NotNull BiConsumer<CommandSender, String> consumer) {
        this.onCmdNotFound = consumer;
    }

    @Override
    public void onNoPermission(@NotNull BiConsumer<CommandSender, SubCommand> consumer) {
        this.noPerm = consumer;
    }

    @Override
    public void onMissingArgs(@NotNull BiConsumer<CommandSender, SubCommand> consumer) {
        this.onMissingArgs = consumer;
    }

    @Override
    public void onNotConsole(@NotNull BiConsumer<Player, SubCommand> consumer) {
        this.onNotConsole = consumer;
    }

    @Override
    public void onNotPlayer(@NotNull BiConsumer<CommandSender, SubCommand> consumer) {
        this.notPlayer = consumer;
    }

    @Override
    public void onExecuteError(@NotNull BiConsumer<CommandSender, String> consumer) {
        this.onExecuteError = consumer;
    }

    @Override
    public void register(@NotNull SubCommand subCommand) {
        Command cmd = subCommand.getCommand();
        if (cmd != null) {
            final String name = cmd.name().toLowerCase();

            if (!this.subcommands.containsKey(name)) {
                this.subcommands.put(name, subCommand);
            }

            for (String alias : cmd.aliases()) {
                if (!this.subcommands.containsKey(alias)) {
                    this.subcommands.put(alias, subCommand);
                }
            }
        }
    }

    @Override
    public void unregister(@NotNull SubCommand subCommand) {
        Command cmd = subCommand.getCommand();
        if (cmd != null) {
            this.subcommands.remove(cmd.name());
            for (String alias : cmd.aliases()) {
                this.subcommands.remove(alias);
            }
        }
    }

    @Override
    public void unregisterAll() {
        this.subcommands.clear();
    }

    @Override
    public Map<String, SubCommand> getSubCommands() {
        return this.subcommands;
    }

    @Override
    public SubCommand getSubCommand(@NotNull String name) {
        return this.subcommands.getOrDefault(name.toLowerCase(), null);
    }

    public boolean hasPermission(@NotNull final CommandSender sender, @NotNull final String perm) {
        if (!(sender instanceof Player player)) return true;
        return player.isOp() || player.hasPermission("*") || player.hasPermission(perm);
    }
}
