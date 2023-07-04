package me.lamtinn.hypelib.hook;

import me.lamtinn.hypelib.plugin.HypePlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public interface Hook {
    void run();

    boolean isEnabled();

    default void print(@NotNull String msg) {
        HypePlugin.plugin.getLogger().log(Level.INFO, msg);
    }
}
