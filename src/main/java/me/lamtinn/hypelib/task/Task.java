package me.lamtinn.hypelib.task;

import me.lamtinn.hypelib.plugin.HypePlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public abstract class Task extends BukkitRunnable {

    public final HypePlugin plugin;

    private final boolean loop;
    private final boolean async;

    private int time;
    private int countdown;

    public Task(@NotNull final HypePlugin plugin, final int time, final boolean loop, final boolean async) {
        this.plugin = plugin;
        this.time = time;
        this.countdown = time;
        this.loop = loop;
        this.async = async;

        if (this.async) {
            this.runTaskTimerAsynchronously(this.plugin, 0L, 20L);
        } else {
            this.runTaskTimer(this.plugin, 0L, 20L);
        }
    }

    public void onRun() {
    }

    public abstract void onFinish();

    public final void run() {
        this.onRun();
        final int countdown = this.countdown - 1;
        this.countdown = countdown;

        if (countdown > 0) {
            return;
        }
        this.countdown = this.time;
        this.onFinish();
        if (!this.loop) {
            this.cancel();
        }
    }

    public final synchronized void restart() {
        this.countdown = this.time;
    }

    public void setTime(final int time) {
        this.time = time;
    }

    public @NotNull HypePlugin getPlugin() {
        return this.plugin;
    }

    public int getTime() {
        return this.time;
    }

    public boolean isAsync() {
        return this.async;
    }

    public boolean isLoop() {
        return this.loop;
    }

    public int getCountdown() {
        return this.countdown;
    }
}
