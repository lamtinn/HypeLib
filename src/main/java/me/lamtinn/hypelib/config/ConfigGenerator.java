package me.lamtinn.hypelib.config;

import me.lamtinn.hypelib.utils.AdventureUtils;
import me.lamtinn.hypelib.plugin.HypePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class ConfigGenerator {

    @Nullable
    public static FileConfiguration newConfig(@NotNull final String name, @NotNull final String dir) {
        File configFile = getFile(name, dir);

        if (configFile != null) {
            return YamlConfiguration.loadConfiguration(configFile);
        } else {
            return null;
        }
    }

    @Nullable
    public static FileConfiguration newConfig(@NotNull final String name) {
        File configFile = getFile(name);

        if (configFile != null) {
            return YamlConfiguration.loadConfiguration(configFile);
        } else {
            return null;
        }
    }

    @Nullable
    private static File getFile(@NotNull final String name, @NotNull final String dir) {
        try {
            File file = new File(HypePlugin.plugin.getDataFolder() + "/" + dir.trim(), name.trim());

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                copyDefaultConfig(name, dir, file);
            }

            return file;
        } catch (Exception e) {
            AdventureUtils.consoleMessage("{prefix} <gray>An error occurred while loading the <#FF416C>" + dir + "/" + name + " <gray>file!");
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    private static File getFile(@NotNull final String name) {
        try {
            String str = name.trim().toLowerCase();
            File file = new File(HypePlugin.plugin.getDataFolder(), str);

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                copyDefaultConfig(name, "", file);
            }

            return file;
        } catch (Exception e) {
            AdventureUtils.consoleMessage("{prefix} <gray>An error occurred while loading the <#FF416C>" + name + " <gray>file!");
            e.printStackTrace();
            return null;
        }
    }

    private static void copyDefaultConfig(@NotNull final String name, @NotNull final String dir, @NotNull final File targetFile) {
        try {
            String filePath = dir.isEmpty() ? name.trim().toLowerCase() : dir.trim() + "/" + name.trim();
            if (HypePlugin.plugin.getResource(filePath) != null) {
                HypePlugin.plugin.saveResource(filePath, false);
            } else {
                targetFile.createNewFile();
            }
        } catch (IOException e) {
            AdventureUtils.consoleMessage("{prefix} <gray>An error occurred while creating the default config for <#FF416C>" + name + " <gray>file!");
            e.printStackTrace();
        }
    }
}
