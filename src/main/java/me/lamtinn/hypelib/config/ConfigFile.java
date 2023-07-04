package me.lamtinn.hypelib.config;

import me.lamtinn.hypelib.config.interfaces.Config;
import me.lamtinn.hypelib.plugin.HypePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigFile implements Config {

    private final HypePlugin plugin;
    private final String name, filename, dir;

    private FileConfiguration config;
    private File file;

    public ConfigFile(final HypePlugin plugin, @NotNull final String filename, @NotNull final String dir) {
        this.plugin = plugin;
        this.filename = filename;
        this.name = filename.replace(".yml", "");
        this.dir = dir;
    }

    public ConfigFile(final HypePlugin plugin, @NotNull final String filename) {
        this.plugin = plugin;
        this.filename = filename;
        this.name = filename.replace(".yml", "");
        this.dir = null;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public @NotNull String getFilename() {
        return this.filename;
    }

    @Override
    public @NotNull String getFilePath() {
        return dir == null ? filename : dir + "/" + filename;
    }

    @Override
    public @NotNull String getDataPath() {
        return dir == null ? plugin.getDataFolder().toString() : plugin.getDataFolder() + "/" + dir;
    }

    @Override
    public @Nullable String getDirectory() {
        return this.dir;
    }

    @Override
    public @Nullable FileConfiguration getConfig() {
        return this.config;
    }

    @Override
    public @NotNull File getFile() {
        return this.file;
    }

    @Override
    public @NotNull HypePlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public void create() {
        File file = new File(this.getDataPath(), this.filename);
        final FileConfiguration config;

        if (this.dir != null) {
            config = ConfigGenerator.newConfig(this.filename, this.dir);
        } else {
            config = ConfigGenerator.newConfig(this.filename);
        }

        this.file = file;
        this.config = config;
    }

    @Override
    public void save() {
        try {
            this.config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reload() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        InputStream inputStream = plugin.getResource(this.getFilePath());

        if (inputStream != null) {
            YamlConfiguration newfile = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
            config.setDefaults(newfile);
            this.config = config;
        }
    }
}