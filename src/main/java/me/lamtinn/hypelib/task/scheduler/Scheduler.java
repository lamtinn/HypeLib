package me.lamtinn.hypelib.task.scheduler;

import me.lamtinn.hypelib.plugin.HypePlugin;
import me.lamtinn.hypelib.task.scheduler.bukkit.BukkitScheduler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public interface Scheduler {
    static Scheduler plugin(Plugin plugin) {
        return new BukkitScheduler(plugin);
    }

    static <T extends HypePlugin> Scheduler plugin(Class<T> clazz) {
        return plugin(JavaPlugin.getPlugin(clazz));
    }

    static Scheduler providingPlugin(Class<?> clazz) {
        return plugin(JavaPlugin.getProvidingPlugin(clazz));
    }

    static Scheduler current() {
        return providingPlugin(Scheduler.class);
    }

    void cancelAllTasks();

    Runner async();

    Runner sync();

    default Runner runner(boolean async) {
        return async ? async() : sync();
    }
}
