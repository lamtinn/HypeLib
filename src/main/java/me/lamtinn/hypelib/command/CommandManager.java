package me.lamtinn.hypelib.command;

import me.lamtinn.hypelib.utils.AdventureUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CommandManager {

    private final JavaPlugin plugin;
    private final Map<String, Command> registered;

    public CommandManager(@NotNull final JavaPlugin plugin) {
        this.plugin = plugin;
        this.registered = new ConcurrentHashMap<>();
    }

    public void register(@NotNull final Command command) {
        String name = command.getLabel();
        if (this.registered.containsKey(name)) {
            AdventureUtils.consoleMessage("{prefix} <gray>Command with name <#FF416C>" + command.getName() + " <gray>has been duplicated!");
            return;
        }

        registerCommandToCommandMap(this.plugin.getName(), command);
        this.registered.put(name, command);
    }

    public void unregister(@NotNull final Command command) {
        unregisterFromKnownCommands(command);
        this.registered.remove(command.getLabel());
    }

    public void unregister(@NotNull final String command) {
        if (this.registered.containsKey(command)) {
            unregister(this.registered.remove(command));
        }
    }

    public void unregisterAll() {
        this.registered.values().forEach(this::unregisterFromKnownCommands);
        this.registered.clear();
    }

    public void syncCommand() {
        try {
            Class<?> craftServer = Bukkit.getServer().getClass();
            Method syncCommandsMethod = craftServer.getDeclaredMethod("syncCommands");
            syncCommandsMethod.setAccessible(true);
            syncCommandsMethod.invoke(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unregisterFromKnownCommands(@NotNull final Command command) {
        try {
            Method getKnownCommandsMethod = SimpleCommandMap.class.getDeclaredMethod("getKnownCommands");
            getKnownCommandsMethod.setAccessible(true);
            Map<?, ?> knownCommands = (Map<?, ?>) getKnownCommandsMethod.invoke(getCommandMap());

            knownCommands.values().removeIf(command::equals);
            command.unregister(getCommandMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerCommandToCommandMap(@NotNull final String label, @NotNull final Command command) {
        getCommandMap().register(label, command);
    }

    private CommandMap getCommandMap() {
        try {
            Method commandMapMethod = Bukkit.getServer().getClass().getMethod("getCommandMap");
            return (CommandMap) commandMapMethod.invoke(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    public Map<String, Command> getRegistered() {
        return Collections.unmodifiableMap(this.registered);
    }
}
