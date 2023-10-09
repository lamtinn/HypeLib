package me.lamtinn.hypelib.database;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DatabaseCredentials {

    @NotNull
    public static DatabaseCredentials of(@NotNull final ConfigurationSection section) {
        return new DatabaseCredentials(section);
    }

    @NotNull
    public static DatabaseCredentials of(@NotNull final FileConfiguration config, @NotNull String path) {
        return DatabaseCredentials.of(config.getConfigurationSection(path));
    }

    private final ConfigurationSection section;

    private DatabaseCredentials(@NotNull final ConfigurationSection section) {
        this.section = section;
    }

    public @Nullable Object get(@NotNull final String value) {
        return this.section.get(value, null);
    }

    public @Nullable Object get(@NotNull final String value, @Nullable final Object defaultValue) {
        return this.section.get(value, defaultValue);
    }

    public @Nullable DatabaseType getType() {
        return DatabaseType.from(this.section.getString("type", "SQLite"));
    }

    public String getDatabase() {
        return this.section.getString("db");
    }

    public String getHost() {
        return this.section.getString("host");
    }

    public int getPort() {
        return this.section.getInt("port");
    }

    public String getUsername() {
        return this.section.getString("username");
    }

    public String getPassword() {
        return this.section.getString("password");
    }

    public String getMongoConnectString() {
        return this.section.getString("mongo-connect-str");
    }

    public int getMinConnections() {
        return this.section.getInt("pool.min-connections");
    }

    public int getMaxConnections() {
        return this.section.getInt("pool.max-connections");
    }

    public @NotNull ConfigurationSection getSection() {
        return this.section;
    }

    @Override
    public String toString() {
        return "DatabaseCredentials{type=" + getType().getName() + ",host=" + getHost() + ",port=" + getPort() + ",username=" + getUsername() + ",password=" + getPassword() + "}";
    }
}
