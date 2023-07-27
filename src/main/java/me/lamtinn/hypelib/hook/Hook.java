package me.lamtinn.hypelib.hook;

import me.lamtinn.hypelib.plugin.HypePlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public interface Hook {
    void run();

    static boolean isEnabled() {
        Plugin plugin = Hook.class.getAnnotation(Plugin.class);
        if (plugin != null) {
            return HypePlugin.plugin
                    .getServer()
                    .getPluginManager()
                    .isPluginEnabled(plugin.value());
        }
        return false;
    };

    default void print(@NotNull String msg) {
        HypePlugin.plugin.getLogger().log(Level.INFO, msg);
    }
}
