package me.lamtinn.hypelib.plugin;

import me.lamtinn.hypelib.command.CommandManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HypePlugin extends BukkitPlugin {

    public static HypePlugin plugin;
    public static BukkitAudiences adventure;

    private final CommandManager commandManager = new CommandManager(this);

    public HypePlugin() {
        super();
    }

    @Override
    public final void onLoad() {
        super.onLoad();
        plugin = this;
    }

    @Override
    public final void onEnable() {
        super.onEnable();
        adventure = BukkitAudiences.create(this);
        this.addEnabledFunction(CommandManager::syncCommand);
    }

    @Override
    public final void onDisable() {
        super.onDisable();
        commandManager.unregisterAll();
        plugin = null;
    }

    public void registerCommand(@NotNull final Command command) {
        commandManager.register(command);
    }

    public @Nullable Command getRegisteredCommand(String label) {
        Command command = commandManager.getRegistered().get(label);
        return command != null ? command : getCommand(label);
    }

    public @NotNull CommandManager getCommandManager() {
        return this.commandManager;
    }
}
