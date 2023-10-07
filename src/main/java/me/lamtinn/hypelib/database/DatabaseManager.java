package me.lamtinn.hypelib.database;

import me.lamtinn.hypelib.database.connector.Connector;
import me.lamtinn.hypelib.plugin.HypePlugin;
import org.jetbrains.annotations.NotNull;

public class DatabaseManager {

    private final DatabaseCredentials credentials;
    private final Connector connector;

    public DatabaseManager(@NotNull final Connector connector) {
        this.connector = connector;
        this.credentials = connector.getCredentials();
    }

    public <T extends HypePlugin> void init(@NotNull final T plugin) {
        connector.connect();
        connector.setupDatabase();
        plugin.registerProvider(Connector.class, this.connector);
    }

    public void disconnect() {
        this.connector.disconnect();
    }

    public @NotNull Connector getConnector() {
        return this.connector;
    }

    public @NotNull DatabaseCredentials getCredentials() {
        return credentials;
    }

    public boolean isConnected() {
        return this.connector.isConnected();
    }
}
