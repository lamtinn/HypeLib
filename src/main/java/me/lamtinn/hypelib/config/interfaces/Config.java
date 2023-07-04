package me.lamtinn.hypelib.config.interfaces;

import me.lamtinn.hypelib.plugin.HypePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public interface Config {
    @NotNull String getName();

    @NotNull String getFilename();

    @NotNull String getFilePath();

    @NotNull String getDataPath();

    @Nullable String getDirectory();

    @Nullable FileConfiguration getConfig();

    @NotNull File getFile();

    @NotNull HypePlugin getPlugin();

    void create();

    void save();

    void reload();
}
