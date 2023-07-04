package me.lamtinn.hypelib.task.scheduler;

import org.bukkit.plugin.Plugin;

public interface ScheTask {
    boolean isCancelled();

    void cancel();

    boolean isAsync();

    boolean isRepeating();

    Plugin getPlugin();
}
