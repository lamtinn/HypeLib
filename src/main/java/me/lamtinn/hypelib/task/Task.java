package me.lamtinn.hypelib.task;

import me.lamtinn.hypelib.plugin.HypePlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public abstract class Task extends BukkitRunnable {

    protected final HypePlugin plugin;
    private int initialTime;
    private int timeRemaining;
    private final boolean loop;
    private final boolean async;

    public Task(@NotNull final HypePlugin plugin, final int initialTime, final boolean loop, final boolean async) {
        this.plugin = plugin;
        this.initialTime = initialTime;
        this.timeRemaining = initialTime;
        this.loop = loop;
        this.async = async;

        if (this.async) {
            this.runTaskTimerAsynchronously(this.plugin, 0L, 20L);
        } else {
            this.runTaskTimer(this.plugin, 0L, 20L);
        }
    }

    public abstract void onFinish();

    @Override
    public final void run() {
        this.onRun();
        this.timeRemaining--;

        if (this.timeRemaining <= 0) {
            this.onFinish();
            if (!this.loop) {
                this.cancel();
            } else {
                this.timeRemaining = this.initialTime;
            }
        }
    }

    public final synchronized void restart() {
        this.timeRemaining = this.initialTime;
    }

    public void setTime(final int time) {
        this.initialTime = time;
        this.timeRemaining = time;
    }

    public @NotNull HypePlugin getPlugin() {
        return this.plugin;
    }

    public int getInitialTime() {
        return this.initialTime;
    }

    public int getTimeRemaining() {
        return this.timeRemaining;
    }

    public boolean isAsync() {
        return this.async;
    }

    public boolean isLoop() {
        return this.loop;
    }

    public abstract void onRun();
}
