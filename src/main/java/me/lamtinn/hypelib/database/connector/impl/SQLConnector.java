package me.lamtinn.hypelib.database.connector.impl;

import me.lamtinn.hypelib.database.DatabaseCredentials;
import me.lamtinn.hypelib.database.connector.Connector;
import me.lamtinn.hypelib.plugin.HypePlugin;
import me.lamtinn.hypelib.utils.AdventureUtils;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.Optional;

public abstract class SQLConnector implements Connector {

    private final String type;
    private final DatabaseCredentials credentials;

    protected Connection connection;

    public SQLConnector(@NotNull final String type, @NotNull final DatabaseCredentials credentials) {
        this.type = type;
        this.credentials = credentials;
    }

    public abstract void init() throws Exception;

    public abstract void setup(final Connection connection) throws SQLException;

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
            this.setup(this.getConnection());
        } catch (SQLException e) {
            AdventureUtils.consoleMessage("{prefix} <gray>Could not setup database: <#FF416C>" + e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        if (!isConnected()) return;
        try {
            this.connection.close();
            AdventureUtils.consoleMessage("{prefix} <gray>Connection to <#FDC830>" + getType() + " <gray>database has been closed!");
        } catch (SQLException e) {
            AdventureUtils.consoleMessage("{prefix} <gray>The database could not be closed: <#FF416C>" + e.getMessage());
        }
    }

    @Override
    public boolean isConnected() {
        return this.connection != null;
    }

    @Override
    public DatabaseCredentials getCredentials() {
        return this.credentials;
    }

    public @NotNull Connection getConnection() {
        return this.connection;
    }

    public Optional<ResultSet> query(@NotNull final String query) {
        try {
            Statement statement = this.connection.createStatement();
            if (statement.execute(query)) {
                return Optional.of(statement.getResultSet());
            }
            int i = statement.getUpdateCount();
            return Optional.of(this.connection.createStatement().executeQuery("SELECT " + i));
        } catch (SQLException e) {
            AdventureUtils.consoleMessage("{prefix} <gray>Failed to execute query(): <#FF416C>" + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Integer> update(@NotNull final String query) {
        try {
            return Optional.of(this.connection.createStatement().executeUpdate(query));
        } catch (SQLException e) {
            AdventureUtils.consoleMessage("{prefix} <gray>Failed to execute update(): <#FF416C>" + e.getMessage());
            return Optional.of(-1);
        }
    }

    public PreparedStatement prepare(@NotNull final String param) {
        try {
            return this.connection.prepareStatement(param);
        } catch (SQLException e) {
            AdventureUtils.consoleMessage("{prefix} <gray>Failed to execute prepare(): <#FF416C>" + e.getMessage());
            return null;
        }
    }

    public boolean isTableExists(@NotNull final String table) {
        try {
            DatabaseMetaData metaData = this.connection.getMetaData();
            try (ResultSet tables = metaData.getTables(null, null, table, null)) {
                return tables.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }
}
