package me.lamtinn.hypelib.command;

import me.lamtinn.hypelib.utils.AdventureUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Source: HSGamer/HSCore
 */
public final class CommandManager {

    private static final Supplier<CommandMap> COMMAND_MAP_SUPPLIER;
    private static final Supplier<Map<?, ?>> KNOWN_COMMANDS_SUPPLIER;
    private static final Runnable SYNC_COMMANDS_RUNNABLE;

    static {
        Method commandMapMethod;
        try {
            commandMapMethod = Bukkit.getServer().getClass().getMethod("getCommandMap");
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }

        COMMAND_MAP_SUPPLIER = () -> {
            try {
                return (CommandMap) commandMapMethod.invoke(Bukkit.getServer());
            } catch (ReflectiveOperationException e) {
                throw new ExceptionInInitializerError(e);
            }
        };

        Supplier<Map<?, ?>> knownCommandsSupplier;
        try {
            Method knownCommandsMethod = SimpleCommandMap.class.getDeclaredMethod("getKnownCommands");
            knownCommandsSupplier = () -> {
                try {
                    return (Map<?, ?>) knownCommandsMethod.invoke(COMMAND_MAP_SUPPLIER.get());
                } catch (ReflectiveOperationException e) {
                    throw new ExceptionInInitializerError(e);
                }
            };
        } catch (NoSuchMethodException e) {
            try {
                Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                knownCommandsSupplier = () -> {
                    try {
                        return (Map<?, ?>) knownCommandsField.get(COMMAND_MAP_SUPPLIER.get());
                    } catch (ReflectiveOperationException ex) {
                        throw new ExceptionInInitializerError(ex);
                    }
                };
            } catch (ReflectiveOperationException ex) {
                throw new ExceptionInInitializerError(ex);
            }
        }
        KNOWN_COMMANDS_SUPPLIER = knownCommandsSupplier;

        Runnable syncCommandsRunnable;
        try {
            Class<?> craftServer = Bukkit.getServer().getClass();
            Method syncCommandsMethod = craftServer.getDeclaredMethod("syncCommands");
            syncCommandsMethod.setAccessible(true);
            syncCommandsRunnable = () -> {
                try {
                    syncCommandsMethod.invoke(Bukkit.getServer());
                } catch (ReflectiveOperationException e) {
                    AdventureUtils.consoleMessage("{prefix} <gray>An error occurred while executing the commands: <#FF416C>" + e.getMessage());
                }
            };
        } catch (Exception e) {
            syncCommandsRunnable = () -> {
            };
        }
        SYNC_COMMANDS_RUNNABLE = syncCommandsRunnable;
    }

    private final JavaPlugin plugin;
    private final Map<String, Command> registered;

    public CommandManager(@NotNull final JavaPlugin plugin) {
        this.plugin = plugin;
        this.registered = new HashMap<>();
    }

    public static void syncCommand() {
        SYNC_COMMANDS_RUNNABLE.run();
    }

    public static void unregisterFromKnownCommands(@NotNull final Command command) {
        Map<?, ?> knownCommands = KNOWN_COMMANDS_SUPPLIER.get();
        knownCommands.values().removeIf(command::equals);
        command.unregister(COMMAND_MAP_SUPPLIER.get());
    }

    public static void registerCommandToCommandMap(@NotNull final String label, @NotNull final Command command) {
        COMMAND_MAP_SUPPLIER.get().register(label, command);
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
        this.registered.values().forEach(CommandManager::unregisterFromKnownCommands);
        this.registered.clear();
    }

    @NotNull
    public Map<String, Command> getRegistered() {
        return Collections.unmodifiableMap(this.registered);
    }
}
