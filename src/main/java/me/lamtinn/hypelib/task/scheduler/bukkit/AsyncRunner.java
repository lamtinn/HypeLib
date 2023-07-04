package me.lamtinn.hypelib.task.scheduler.bukkit;

import me.lamtinn.hypelib.task.scheduler.Runner;
import me.lamtinn.hypelib.task.scheduler.ScheTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

public class AsyncRunner implements Runner {

    private final BukkitScheduler scheduler;

    AsyncRunner(@NotNull final BukkitScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public ScheTask runTask(@NotNull Runnable runnable) {
        return scheduler.wrapTask(scheduler.wrapRunnable(runnable).runTaskAsynchronously(scheduler.getPlugin()), false);
    }

    @Override
    public ScheTask runTaskLater(@NotNull Runnable runnable, long delay) {
        return scheduler.wrapTask(scheduler.wrapRunnable(runnable).runTaskLaterAsynchronously(scheduler.getPlugin(), delay), false);
    }

    @Override
    public ScheTask runTaskTimer(@NotNull BooleanSupplier runnable, long delay, long period) {
        return scheduler.wrapTask(scheduler.wrapRunnable(runnable).runTaskTimerAsynchronously(scheduler.getPlugin(), delay, period), false);
    }
}
