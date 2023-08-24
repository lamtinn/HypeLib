package me.lamtinn.hypelib.task.scheduler;

import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

public interface Runner {
    ScheTask runTask(@NotNull final Runnable runnable);

    ScheTask runTaskLater(@NotNull final Runnable runnable, final long delay);

    ScheTask runTaskTimer(@NotNull final BooleanSupplier runnable, final long delay, final long period);

    default ScheTask runTaskTimer(@NotNull final Runnable runnable, final long delay, final long period) {
        return this.runTaskTimer(() -> {
            runnable.run();
            return true;
        }, delay, period);
    }
}
