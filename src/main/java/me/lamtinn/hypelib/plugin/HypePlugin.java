package me.lamtinn.hypelib.plugin;

import me.lamtinn.hypelib.action.Action;
import me.lamtinn.hypelib.action.ActionManager;
import me.lamtinn.hypelib.command.CommandManager;
import me.lamtinn.hypelib.config.ConfigFile;
import me.lamtinn.hypelib.config.ConfigManager;
import me.lamtinn.hypelib.task.scheduler.Scheduler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Level;

public class HypePlugin extends BukkitPlugin {

    public static HypePlugin plugin;
    public static BukkitAudiences adventure;

    private final CommandManager commandManager = new CommandManager(this);
    private final ConfigManager configManager = new ConfigManager(this);
    private final ActionManager actionManager = new ActionManager(this);

    private Set<Permission> registeredPermissions = new HashSet<>();

    public HypePlugin() {
        super();
    }

    @Override
    public final void onLoad() {
        plugin = this;
        super.onLoad();
    }

    @Override
    public final void onEnable() {
        adventure = BukkitAudiences.create(this);
        super.onEnable();

        this.registerPermissions();
        this.addEnabledFunction(commandManager::syncCommand);
    }

    @Override
    public final void onDisable() {
        super.onDisable();

        commandManager.unregisterAll();

        registeredPermissions.forEach(Bukkit.getPluginManager()::removePermission);

        configManager.save();
        configManager.unregisterAll();

        plugin = null;
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
    }

    public void disable() {
        this.getPluginLoader().disablePlugin(this);
    }

    public void registerCommand(@NotNull final Command command) {
        commandManager.register(command);
    }

    public void registerConfig(@NotNull final ConfigFile configFile, @NotNull final String name) {
        this.reloadConfig();

        configFile.create();
        configFile.getConfig().options().copyDefaults();
        configFile.save();

        this.saveDefaultConfig();
        this.configManager.register(configFile, name);
    }

    public void registerConfig(@NotNull final ConfigFile configFile, @NotNull final String ... names) {
        this.reloadConfig();

        configFile.create();
        configFile.getConfig().options().copyDefaults();
        configFile.save();

        this.saveDefaultConfig();

        this.configManager.register(configFile, names);
        this.configManager.save();
    }

    public void registerAction(@NotNull final Action action) {
        this.actionManager.register(action);
    }

    public void registerPermission(@NotNull final Permission permission) {
        Bukkit.getPluginManager().addPermission(permission);
        this.registeredPermissions.add(permission);
    }

    private void registerPermissions() {
        for (Class<?> clazz : this.getPermissions()) {
            for (Field field : clazz.getDeclaredFields()) {
                int modifiers = field.getModifiers();

                if (!field.getType().equals(Permission.class) || !Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers))
                    continue;

                try {
                    Permission permission = (Permission) field.get(null);
                    registerPermission(permission);
                } catch (IllegalAccessException e) {
                    getLogger().log(Level.WARNING, "Failed to register permission : " + e.getMessage());
                }
            }
        }
    }

    public <T> void registerProvider(Class<T> service, T provider) {
        this.getServer().getServicesManager().register(service, provider, this, ServicePriority.Normal);
    }

    public <T> void registerProvider(Class<T> service, T provider, ServicePriority priority) {
        this.getServer().getServicesManager().register(service, provider, this, priority);
    }

    protected List<Class<?>> getPermissions() {
        return Collections.singletonList(getClass());
    }

    public NamespacedKey key(@NotNull final String key) {
        return new NamespacedKey(this, key);
    }

    public void callEvent(@NotNull final Event event) {
        Scheduler.plugin(this).sync().runTask(() ->
                this.getServer().getPluginManager().callEvent(event)
        );
    }

    public void log(@NotNull final String msg) {
        this.getLogger().log(Level.INFO, msg);
    }

    public void log(@NotNull final String[] msg) {
        Arrays.stream(msg).forEach(this::log);
    }

    public @Nullable Command getRegisteredCommand(String label) {
        Command command = commandManager.getRegistered().get(label);
        return command != null ? command : getCommand(label);
    }

    public @Nullable FileConfiguration getConfig(String label) {
        return this.configManager.get(label);
    }

    public @NotNull CommandManager getCommandManager() {
        return this.commandManager;
    }

    public @NotNull ConfigManager getConfigManager() {
        return this.configManager;
    }

    public @NotNull ActionManager getActionManager() {
        return this.actionManager;
    }
}
