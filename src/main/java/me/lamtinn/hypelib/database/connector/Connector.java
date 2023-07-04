package me.lamtinn.hypelib.database.connector;

import me.lamtinn.hypelib.database.DatabaseCredentials;

public interface Connector {
    String getType();

    void connect();

    void setupDatabase();

    void disconnect();

    boolean isConnected();

    DatabaseCredentials getCredentials();
}
