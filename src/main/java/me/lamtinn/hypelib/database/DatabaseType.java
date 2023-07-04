package me.lamtinn.hypelib.database;

import org.jetbrains.annotations.NotNull;

public enum DatabaseType {
    SQLITE("SQLite", "sqlite"),
    MYSQL("MySQL", "mysql"),
    MONGODB("MongoDB", "mongodb");

    private final String[] names;

    DatabaseType(@NotNull final String... names) {
        this.names = names;
    }

    public @NotNull String[] getNames() {
        return this.names;
    }

    public @NotNull String getName() {
        return this.names[0];
    }

    public static @NotNull DatabaseType from(@NotNull final String name) {
        for (final DatabaseType databaseType : values()) {
            for (final String name2 : databaseType.getNames()) {
                if (name.equalsIgnoreCase(name2)) {
                    return databaseType;
                }
            }
        }
        return DatabaseType.SQLITE;
    }
}
