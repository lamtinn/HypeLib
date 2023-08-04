package me.lamtinn.hypelib.config;

import me.lamtinn.hypelib.plugin.HypePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class ConfigManager {

    private final HypePlugin plugin;
    private final Map<String, ConfigFile> configs;

    public ConfigManager(@NotNull final HypePlugin plugin) {
        this.plugin = plugin;
        this.configs = new HashMap<>();
    }

    public void register(@NotNull final ConfigFile configFile, final String ... names) {
        Arrays.stream(names).forEach(name -> register(configFile, name));
    }

    public void register(@NotNull final ConfigFile configFile, final String name) {
        this.configs.put(name, configFile);
    }

    public void unregister(@NotNull final String name) {
        this.configs.remove(name);
    }

    public void unregister(@NotNull final String[] names) {
        Arrays.stream(names).forEach(this::unregister);
    }

    public void unregisterAll() {
        this.configs.clear();
    }

    public void reload() {
        final Map<String, ConfigFile> newConfigs = new HashMap<>(this.configs);
        this.configs.clear();

        for (final Map.Entry<String, ConfigFile> entry : newConfigs.entrySet()) {
            ConfigFile configFile = entry.getValue();
            FileConfiguration yaml = YamlConfiguration.loadConfiguration(configFile.getFile());

            InputStream inputStream = this.plugin.getResource(configFile.getFilePath());
            if (inputStream != null) {
                YamlConfiguration newFile = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(inputStream)
                );
                yaml.setDefaults(newFile);
                configFile.setFile(yaml);
            }

            newConfigs.put(entry.getKey(), configFile);
        }

        this.configs.putAll(newConfigs);
    }

    public void save() {
        this.configs.values().forEach(ConfigFile::save);
    }

    public boolean has(@NotNull final String name) {
        return this.configs.containsKey(name);
    }

    public @Nullable FileConfiguration get(@NotNull final String name) {
        return this.configs.getOrDefault(name, null).getConfig();
    }

    public @NotNull Collection<ConfigFile> getAll() {
        return this.configs.values();
    }

    public @NotNull Collection<FileConfiguration> getConfigFiles() {
        return this.getAll().stream().map(ConfigFile::getConfig).collect(Collectors.toList());
    }

    public @NotNull Map<String, ConfigFile> getConfigs() {
        return this.configs;
    }
}
