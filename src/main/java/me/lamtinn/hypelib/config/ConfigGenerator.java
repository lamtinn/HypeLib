package me.lamtinn.hypelib.config;

import me.lamtinn.hypelib.utils.AdventureUtils;
import me.lamtinn.hypelib.plugin.HypePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class ConfigGenerator {

    @Nullable
    public static FileConfiguration newConfig(@NotNull final String name, @NotNull final String dir) {
        try {
            File file = new File(HypePlugin.plugin.getDataFolder() + "/" + dir.trim(), name.trim());
            if (!file.exists()) {
                file.getParentFile().mkdirs();

                String s = dir.trim() + "/" + name.trim();

                if (HypePlugin.plugin.getResource(s) != null) {
                    HypePlugin.plugin.saveResource(s, false);
                }
            }
            return YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            AdventureUtils.consoleMessage("{prefix} <gray>An error occurred while loading the <#FF416C>" + dir + "/" + name + " <gray>file!");
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static FileConfiguration newConfig(@NotNull final String name) {
        try {
            String str = name.trim().toLowerCase();
            File file = new File(HypePlugin.plugin.getDataFolder(), str);

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                if (HypePlugin.plugin.getResource(str) != null) {
                    HypePlugin.plugin.saveResource(str, false);
                }
            }
            return YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            AdventureUtils.consoleMessage("{prefix} <gray>An error occurred while loading the <#FF416C>" + name + " <gray>file!");
            e.printStackTrace();
            return null;
        }
    }
}
