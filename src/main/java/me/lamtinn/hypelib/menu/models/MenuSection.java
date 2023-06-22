package me.lamtinn.hypelib.menu.models;

import me.lamtinn.hypelib.menu.interfaces.HMenuSection;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MenuSection implements HMenuSection {

    private final FileConfiguration config;
    private final String key;

    public MenuSection(@NotNull FileConfiguration config, @NotNull String key) {
        this.config = config;
        this.key = key;
    }

    @Override
    public @NotNull FileConfiguration getConfig() {
        return this.config;
    }

    @Override
    public @NotNull String getKey() {
        return this.key;
    }

    @Override
    public @NotNull Set<ConfigurationSection> get() {
        Set<String> keys = config.getConfigurationSection(key).getKeys(false);
        if (config.getConfigurationSection(key) == null || keys.isEmpty()) {
            return Collections.emptySet();
        }
        Set<ConfigurationSection> sections = Arrays.stream(keys.toArray())
                .map(k -> config.getConfigurationSection(key + "." + k))
                .collect(Collectors.toSet());
        sections.removeIf(Objects::isNull);
        return sections;
    }
}
