package me.lamtinn.hypelib.config;

import me.lamtinn.hypelib.config.interfaces.Config;
import me.lamtinn.hypelib.plugin.HypePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class ConfigFile implements Config {

    private final HypePlugin plugin;
    private final String name;
    private final String filename;
    private final String dir;

    private FileConfiguration config;
    private File file;

    public ConfigFile(final HypePlugin plugin, @NotNull final String filename, @Nullable final String dir) {
        this.plugin = plugin;
        this.filename = filename;
        this.name = filename.replace(".yml", "");
        this.dir = dir;
    }

    public ConfigFile(final HypePlugin plugin, @NotNull final String filename) {
        this(plugin, filename, null);
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
        return dir == null ? filename : dir + File.separator + filename;
    }

    @Override
    public @NotNull String getDataPath() {
        return dir == null ? plugin.getDataFolder().toString() : plugin.getDataFolder() + File.separator + dir;
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
            plugin.getLogger().severe("An error occurred while saving the config file: " + this.filename);
            e.printStackTrace();
        }
    }

    @Override
    public void setFile(@NotNull FileConfiguration file) {
        this.config = file;
    }
}
