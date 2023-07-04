package me.lamtinn.hypelib.task.scheduler.bukkit;

import me.lamtinn.hypelib.task.scheduler.Runner;
import me.lamtinn.hypelib.task.scheduler.ScheTask;
import me.lamtinn.hypelib.task.scheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

public class BukkitScheduler implements Scheduler {

    private final AsyncRunner async;
    private final SyncRunner sync;
    private final Plugin plugin;

    public BukkitScheduler(@NotNull final Plugin plugin) {
        this.async = new AsyncRunner(this);
        this.sync = new SyncRunner(this);
        this.plugin = plugin;
    }

    public BukkitRunnable wrapRunnable(@NotNull final Runnable runnable) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    public BukkitRunnable wrapRunnable(@NotNull final BooleanSupplier runnable) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (!runnable.getAsBoolean()) {
                    cancel();
                }
            }
        };
    }

    public ScheTask wrapTask(@NotNull final BukkitTask bukkitTask, boolean repeating) {
        return new ScheTask() {
            boolean cancelled = false;

            @Override
            public boolean isCancelled() {
                try {
                    return bukkitTask.isCancelled();
                } catch (Throwable throwable) {
                    return cancelled;
                }
            }

            @Override
            public void cancel() {
                cancelled = true;
                bukkitTask.cancel();
            }

            @Override
            public boolean isAsync() {
                return !bukkitTask.isSync();
            }

            @Override
            public boolean isRepeating() {
                return repeating;
            }

            @Override
            public Plugin getPlugin() {
                return bukkitTask.getOwner();
            }
        };
    }

    public @NotNull Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void cancelAllTasks() {
        Bukkit.getScheduler().cancelTasks(plugin);
    }

    @Override
    public Runner async() {
        return this.async;
    }

    @Override
    public Runner sync() {
        return this.sync;

    }
}
