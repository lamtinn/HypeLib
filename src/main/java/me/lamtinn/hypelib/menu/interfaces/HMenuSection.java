package me.lamtinn.hypelib.menu.interfaces;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface HMenuSection {
    @NotNull FileConfiguration getConfig();

    @NotNull String getKey();

    @NotNull Set<ConfigurationSection> get();
}
