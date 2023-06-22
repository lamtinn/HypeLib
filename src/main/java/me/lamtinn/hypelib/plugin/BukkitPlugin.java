package me.lamtinn.hypelib.plugin;

import me.lamtinn.hypelib.command.CommandManager;
import me.lamtinn.hypelib.menu.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class BukkitPlugin extends JavaPlugin {

    private final List<Runnable> enabledFunctions;
    private final List<Runnable> disabledFunctions;

    public BukkitPlugin() {
        this.enabledFunctions = new ArrayList<>();
        this.disabledFunctions = new ArrayList<>();

        preLoad();
    }

    @Override
    public void onLoad() {
        this.load();
    }

    @Override
    public void onEnable() {
        this.enable();

        this.registerListener(new MenuListener());
        this.enabledFunctions.forEach(Runnable::run);
    }

    @Override
    public void onDisable() {
        this.disable();

        this.disabledFunctions.forEach(Runnable::run);
        this.disabledFunctions.clear();

        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);

        this.postDisable();
    }

    public void registerListener(final Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    public void addEnabledFunction(final Runnable function) {
        this.enabledFunctions.add(function);
    }

    public void addDisabledFunction(final Runnable function) {
        this.disabledFunctions.add(function);
    }

    public void preLoad() {
    }

    public void load() {
    }

    public void enable() {
    }

    public void postEnable() {
    }

    public void disable() {
    }

    public void postDisable() {
    }
}
