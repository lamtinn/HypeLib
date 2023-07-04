package me.lamtinn.hypelib.database.connector.impl;

import com.mongodb.client.MongoClient;
import me.lamtinn.hypelib.database.DatabaseCredentials;
import me.lamtinn.hypelib.database.connector.Connector;
import me.lamtinn.hypelib.plugin.HypePlugin;
import me.lamtinn.hypelib.utils.AdventureUtils;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public abstract class MongoConnector implements Connector {

    private final String type;
    private final DatabaseCredentials credentials;

    protected MongoClient mongo;

    public MongoConnector(@NotNull final String type, @NotNull final DatabaseCredentials credentials) {
        this.type = type;
        this.credentials = credentials;
    }

    public abstract void init() throws Exception;

    public abstract void setup(final MongoClient client) throws SQLException;

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void connect() {
        if (isConnected()) return;
        try {
            this.init();
        } catch (Exception e) {
            AdventureUtils.consoleMessage("{prefix} <gray>Failed to connect to <#FF416C>" + getType() + " <gray>database!");
            AdventureUtils.consoleMessage("{prefix} <#FF416C>" + e.getMessage());
            HypePlugin.plugin.getPluginLoader().disablePlugin(HypePlugin.plugin);
        }
    }

    @Override
    public void setupDatabase() {
        try {
            this.setup(this.getClient());
        } catch (SQLException e) {
            AdventureUtils.consoleMessage("{prefix} <gray>Could not setup database: <#FF416C>" + e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        if (!isConnected()) return;
        try {
            this.mongo.close();
            AdventureUtils.consoleMessage("{prefix} <gray>Connection to <#FDC830>" + getType() + " <gray>database has been closed!");
        } catch (Exception e) {
            AdventureUtils.consoleMessage("{prefix} <gray>The database could not be closed: <#FF416C>" + e.getMessage());
        }
    }

    @Override
    public boolean isConnected() {
        return this.mongo != null;
    }

    @Override
    public DatabaseCredentials getCredentials() {
        return this.credentials;
    }

    public @NotNull MongoClient getClient() {
        return this.mongo;
    }
}
